package io.github.craftfabric.craftfabric.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import io.github.craftfabric.craftfabric.mixin.ITextFormatMixin;
import net.minecraft.text.*;
import net.minecraft.text.event.ClickEvent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatUtils {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + ChatColor.COLOR_CHAR + " \\n]|$))))");
    private static final Map<Character, TextFormat> formatMap;

    static {
        Builder<Character, TextFormat> builder = ImmutableMap.builder();
        for (TextFormat format : TextFormat.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        formatMap = builder.build();
    }

    public static TextFormat getColor(ChatColor color) {
        return formatMap.get(color.getChar());
    }

    public static ChatColor getColor(TextFormat format) {
        return ChatColor.getByChar(((ITextFormatMixin) (Object) format).getSectionSignCode());
    }

    private static class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + ChatColor.COLOR_CHAR + "[0-9a-fk-or])|(\\n)|((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + ChatColor.COLOR_CHAR + " \\n]|$))))", Pattern.CASE_INSENSITIVE);

        private final List<TextComponent> list = new ArrayList<>();
        private TextComponent currentChatComponent = new StringTextComponent("");
        private Style modifier = new Style();
        private final TextComponent[] output;
        private int currentIndex;
        private final String message;

        private StringMessage(String message, boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                output = new TextComponent[]{currentChatComponent};
                return;
            }
            list.add(currentChatComponent);

            Matcher matcher = INCREMENTAL_PATTERN.matcher(message);
            String match;
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) ;
                appendNewComponent(matcher.start(groupId));
                switch (groupId) {
                    case 1:
                        TextFormat format = formatMap.get(match.toLowerCase(java.util.Locale.ENGLISH).charAt(1));
                        if (format == TextFormat.RESET) {
                            modifier = new Style();
                        } else if (format.isModifier()) {
                            switch (format) {
                                case BOLD:
                                    modifier.setBold(Boolean.TRUE);
                                    break;
                                case ITALIC:
                                    modifier.setItalic(Boolean.TRUE);
                                    break;
                                case STRIKETHROUGH:
                                    modifier.setStrikethrough(Boolean.TRUE);
                                    break;
                                case UNDERLINE:
                                    modifier.setUnderline(Boolean.TRUE);
                                    break;
                                case OBFUSCATED:
                                    modifier.setObfuscated(Boolean.TRUE);
                                    break;
                                default:
                                    throw new AssertionError("Unexpected message format");
                            }
                        } else { // Color resets formatting
                            modifier = new Style().setColor(format);
                        }
                        break;
                    case 2:
                        if (keepNewlines) {
                            currentChatComponent.append(new StringTextComponent("\n"));
                        } else {
                            currentChatComponent = null;
                        }
                        break;
                    case 3:
                        if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                            match = "http://" + match;
                        }
                        modifier.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                        appendNewComponent(matcher.end(groupId));
                        modifier.setClickEvent(null);
                }
                currentIndex = matcher.end(groupId);
            }

            if (currentIndex < message.length()) {
                appendNewComponent(message.length());
            }

            output = list.toArray(new TextComponent[list.size()]);
        }

        private void appendNewComponent(int index) {
            if (index <= currentIndex) {
                return;
            }
            TextComponent addition = new StringTextComponent(message.substring(currentIndex, index)).setStyle(modifier);
            currentIndex = index;
            modifier = modifier.clone();
            if (currentChatComponent == null) {
                currentChatComponent = new StringTextComponent("");
                list.add(currentChatComponent);
            }
            currentChatComponent.append(addition);
        }

        private TextComponent[] getOutput() {
            return output;
        }
    }

    public static TextComponent wrapOrNull(String message) {
        return (message == null || message.isEmpty()) ? null : new StringTextComponent(message);
    }

    public static TextComponent fromStringOrNull(String message) {
        return fromStringOrNull(message, false);
    }

    public static TextComponent fromStringOrNull(String message, boolean keepNewlines) {
        return (message == null || message.isEmpty()) ? null : fromString(message, keepNewlines)[0];
    }

    public static TextComponent[] fromString(String message) {
        return fromString(message, false);
    }

    public static TextComponent[] fromString(String message, boolean keepNewlines) {
        return new StringMessage(message, keepNewlines).getOutput();
    }

    public static String fromComponent(TextComponent component) {
        return fromComponent(component, TextFormat.BLACK);
    }

    public static String toJSON(TextComponent component) {
        return TextComponent.Serializer.toJsonString(component);
    }

    public static String fromComponent(TextComponent component, TextFormat defaultColor) {
        if (component == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();

        for (TextComponent current : component) {
            Style style = current.getStyle();
            out.append(style.getColor() == null ? defaultColor : style.getColor());
            if (style.isBold()) {
                out.append(TextFormat.BOLD);
            }
            if (style.isItalic()) {
                out.append(TextFormat.ITALIC);
            }
            if (style.isUnderlined()) {
                out.append(TextFormat.UNDERLINE);
            }
            if (style.isStrikethrough()) {
                out.append(TextFormat.STRIKETHROUGH);
            }
            if (style.isObfuscated()) {
                out.append(TextFormat.OBFUSCATED);
            }
            out.append(current.getText());
        }
        return out.toString().replaceFirst("^(" + defaultColor + ")*", "");
    }

    public static TextComponent fixComponent(TextComponent component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static TextComponent fixComponent(TextComponent component, Matcher matcher) {
        if (component instanceof StringTextComponent) {
            StringTextComponent text = ((StringTextComponent) component);
            String msg = text.getText();
            if (matcher.reset(msg).find()) {
                matcher.reset();

                Style style = text.getStyle() != null ?
                        text.getStyle() : new Style();
                List<TextComponent> extras = new ArrayList<>();
                List<TextComponent> extrasOld = new ArrayList<>(text.getChildren());
                component = text = new StringTextComponent("");

                int pos = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }

                    StringTextComponent prev = new StringTextComponent(msg.substring(pos, matcher.start()));
                    prev.setStyle(style);
                    extras.add(prev);

                    StringTextComponent link = new StringTextComponent(matcher.group());
                    Style linkStyle = style.clone();
                    linkStyle.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                    link.setStyle(linkStyle);
                    extras.add(link);

                    pos = matcher.end();
                }

                StringTextComponent prev = new StringTextComponent(msg.substring(pos));
                prev.setStyle(style);
                extras.add(prev);
                extras.addAll(extrasOld);

                for (TextComponent current : extras) {
                    text.append(current);
                }
            }
        }

        List<TextComponent> extras = component.getChildren();
        for (int i = 0; i < extras.size(); i++) {
            TextComponent comp = extras.get(i);
            //FIXME
            // if (comp.getStyle() != null && comp.getStyle().h() == null) {
            //      extras.set(i, fixComponent(comp, matcher));
            // }
        }

        if (component instanceof TranslatableTextComponent) {
            Object[] subs = ((TranslatableTextComponent) component).getParams();
            for (int i = 0; i < subs.length; i++) {
                Object comp = subs[i];
                if (comp instanceof TextComponent) {
                    TextComponent c = (TextComponent) comp; //FIXME
                    //  if (c.getStyle() != null && c.getStyle().h() == null) {
                    //      subs[i] = fixComponent(c, matcher);
                    //  }
                } else if (comp instanceof String && matcher.reset((String) comp).find()) {
                    subs[i] = fixComponent(new StringTextComponent((String) comp), matcher);
                }
            }
        }

        return component;
    }

    private ChatUtils() {
    }
}
