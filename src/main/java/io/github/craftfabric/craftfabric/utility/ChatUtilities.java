package io.github.craftfabric.craftfabric.utility;

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

public final class ChatUtilities {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + ChatColor.COLOR_CHAR + " \\n]|$))))");
    private static final Map<Character, TextFormat> FORMAT_MAP;

    static {
        Builder<Character, TextFormat> builder = ImmutableMap.builder();
        for (TextFormat format : TextFormat.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        FORMAT_MAP = builder.build();
    }

    public static TextFormat getColor(ChatColor color) {
        return FORMAT_MAP.get(color.getChar());
    }

    public static ChatColor getColor(TextFormat format) {
        return ChatColor.getByChar(((ITextFormatMixin) (Object) format).getSectionSignCode());
    }

    private static class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + ChatColor.COLOR_CHAR + "[0-9a-fk-or])|(\\n)|((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + ChatColor.COLOR_CHAR + " \\n]|$))))", Pattern.CASE_INSENSITIVE);

        private final List<TextComponent> list = new ArrayList<>();
        private TextComponent currentChatComponent = new StringTextComponent("");
        private Style style = new Style();
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
                        TextFormat format = FORMAT_MAP.get(match.toLowerCase(java.util.Locale.ENGLISH).charAt(1));
                        if (format == TextFormat.RESET) {
                            style = new Style();
                        } else if (format.isModifier()) {
                            switch (format) {
                                case BOLD:
                                    style.setBold(Boolean.TRUE);
                                    break;
                                case ITALIC:
                                    style.setItalic(Boolean.TRUE);
                                    break;
                                case STRIKETHROUGH:
                                    style.setStrikethrough(Boolean.TRUE);
                                    break;
                                case UNDERLINE:
                                    style.setUnderline(Boolean.TRUE);
                                    break;
                                case OBFUSCATED:
                                    style.setObfuscated(Boolean.TRUE);
                                    break;
                                default:
                                    throw new AssertionError("Unexpected message format");
                            }
                        } else { // Color resets formatting
                            style = new Style().setColor(format);
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
                        style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                        appendNewComponent(matcher.end(groupId));
                        style.setClickEvent(null);
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
            TextComponent addition = new StringTextComponent(message.substring(currentIndex, index)).setStyle(style);
            currentIndex = index;
            style = style.clone();
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
        StringBuilder result = new StringBuilder();

        for (TextComponent current : component) {
            Style style = current.getStyle();
            result.append(style.getColor() == null ? defaultColor : style.getColor());
            if (style.isBold()) {
                result.append(TextFormat.BOLD);
            }
            if (style.isItalic()) {
                result.append(TextFormat.ITALIC);
            }
            if (style.isUnderlined()) {
                result.append(TextFormat.UNDERLINE);
            }
            if (style.isStrikethrough()) {
                result.append(TextFormat.STRIKETHROUGH);
            }
            if (style.isObfuscated()) {
                result.append(TextFormat.OBFUSCATED);
            }
            result.append(current.getText());
        }
        return result.toString().replaceFirst("^(" + defaultColor + ")*", "");
    }

    public static TextComponent fixComponent(TextComponent component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static TextComponent fixComponent(TextComponent component, Matcher matcher) {
        if (component instanceof StringTextComponent) {
            StringTextComponent text = ((StringTextComponent) component);
            String message = text.getText();
            if (matcher.reset(message).find()) {
                matcher.reset();

                Style style = text.getStyle() != null ? text.getStyle() : new Style();
                List<TextComponent> extras = new ArrayList<>();
                List<TextComponent> extrasOld = new ArrayList<>(text.getSiblings());
                component = text = new StringTextComponent("");

                int position = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }

                    StringTextComponent previous = new StringTextComponent(message.substring(position, matcher.start()));
                    previous.setStyle(style);
                    extras.add(previous);

                    StringTextComponent link = new StringTextComponent(matcher.group());
                    Style linkStyle = style.clone();
                    linkStyle.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                    link.setStyle(linkStyle);
                    extras.add(link);

                    position = matcher.end();
                }

                StringTextComponent previous = new StringTextComponent(message.substring(position));
                previous.setStyle(style);
                extras.add(previous);
                extras.addAll(extrasOld);

                for (TextComponent current : extras) {
                    text.append(current);
                }
            }
        }

        List<TextComponent> extras = component.getSiblings();
        for (int i = 0; i < extras.size(); i++) {
            TextComponent currentComponent = extras.get(i);
            if (currentComponent.getStyle() != null && currentComponent.getStyle().getClickEvent() == null) {
                extras.set(i, fixComponent(currentComponent, matcher));
            }
        }

        if (component instanceof TranslatableTextComponent) {
            Object[] parameters = ((TranslatableTextComponent) component).getParams();
            for (int i = 0; i < parameters.length; i++) {
                Object currentParameter = parameters[i];
                if (currentParameter instanceof TextComponent) {
                    TextComponent componentParameter = (TextComponent) currentParameter;
                    if (componentParameter.getStyle() != null && componentParameter.getStyle().getClickEvent() == null) {
                        parameters[i] = fixComponent(componentParameter, matcher);
                    }
                } else if (currentParameter instanceof String && matcher.reset((String) currentParameter).find()) {
                    parameters[i] = fixComponent(new StringTextComponent((String) currentParameter), matcher);
                }
            }
        }

        return component;
    }

    private ChatUtilities() {
    }
}
