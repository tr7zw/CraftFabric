package io.github.craftfabric.craftfabric.utility;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import io.github.craftfabric.craftfabric.accessor.util.FormattingAccessor;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatUtilities {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + ChatColor.COLOR_CHAR + " \\n]|$))))");
    private static final Map<Character, Formatting> FORMAT_MAP;

    static {
        Builder<Character, Formatting> builder = ImmutableMap.builder();
        for (Formatting format : Formatting.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        FORMAT_MAP = builder.build();
    }

    public static Formatting getColor(ChatColor color) {
        return FORMAT_MAP.get(color.getChar());
    }

    public static ChatColor getColor(Formatting format) {
        return ChatColor.getByChar(((FormattingAccessor) (Object) format).getCode());
    }

    private static class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + ChatColor.COLOR_CHAR + "[0-9a-fk-or])|(\\n)|((?:(?:https?)://)?(?:[-\\w_.]{2,}\\.[a-z]{2,4}.*?(?=[.?!,;:]?(?:[" + ChatColor.COLOR_CHAR + " \\n]|$))))", Pattern.CASE_INSENSITIVE);

        private final List<Text> list = new ArrayList<>();
        private Text currentChatComponent = new net.minecraft.text.LiteralText("");
        private Style style = new Style();
        private final Text[] output;
        private int currentIndex;
        private final String message;

        private StringMessage(String message, boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                output = new Text[]{currentChatComponent};
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
                    	Formatting format = FORMAT_MAP.get(match.toLowerCase(java.util.Locale.ENGLISH).charAt(1));
                        if (format == Formatting.RESET) {
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
                            currentChatComponent.append(new LiteralText("\n"));
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

            output = list.toArray(new Text[list.size()]);
        }

        private void appendNewComponent(int index) {
            if (index <= currentIndex) {
                return;
            }
            Text addition = new LiteralText(message.substring(currentIndex, index)).setStyle(style);
            currentIndex = index;
            style = style.copy();
            if (currentChatComponent == null) {
                currentChatComponent = new LiteralText("");
                list.add(currentChatComponent);
            }
            currentChatComponent.append(addition);
        }

        private Text[] getOutput() {
            return output;
        }
    }

    public static Text wrapOrNull(String message) {
        return (message == null || message.isEmpty()) ? null : new LiteralText(message);
    }

    public static Text fromStringOrNull(String message) {
        return fromStringOrNull(message, false);
    }

    public static Text fromStringOrNull(String message, boolean keepNewlines) {
        return (message == null || message.isEmpty()) ? null : fromString(message, keepNewlines)[0];
    }

    public static Text[] fromString(String message) {
        return fromString(message, false);
    }

    public static Text[] fromString(String message, boolean keepNewlines) {
        return new StringMessage(message, keepNewlines).getOutput();
    }

    public static String fromComponent(Text component) {
        return fromComponent(component, Formatting.BLACK);
    }

    public static String toJSON(Text component) {
        return Text.Serializer.toJson(component);
    }

    public static String fromComponent(Text component, Formatting defaultColor) {
        if (component == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();

        for (Text current : component) {
            Style style = current.getStyle();
            result.append(style.getColor() == null ? defaultColor : style.getColor());
            if (style.isBold()) {
                result.append(Formatting.BOLD);
            }
            if (style.isItalic()) {
                result.append(Formatting.ITALIC);
            }
            if (style.isUnderlined()) {
                result.append(Formatting.UNDERLINE);
            }
            if (style.isStrikethrough()) {
                result.append(Formatting.STRIKETHROUGH);
            }
            if (style.isObfuscated()) {
                result.append(Formatting.OBFUSCATED);
            }
            result.append(current.getString());
        }
        return result.toString().replaceFirst("^(" + defaultColor + ")*", "");
    }

    public static Text fixComponent(Text component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static Text fixComponent(Text component, Matcher matcher) {
        if (component instanceof net.minecraft.text.LiteralText) {
        	LiteralText text = ((LiteralText) component);
            String message = text.asFormattedString();
            if (matcher.reset(message).find()) {
                matcher.reset();

                Style style = text.getStyle() != null ? text.getStyle() : new Style();
                List<Text> extras = new ArrayList<>();
                List<Text> extrasOld = new ArrayList<>(text.getSiblings());
                component = text = new LiteralText("");

                int position = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                        match = "http://" + match;
                    }

                    Text previous = new LiteralText(message.substring(position, matcher.start()));
                    previous.setStyle(style);
                    extras.add(previous);

                    Text link = new LiteralText(matcher.group());
                    Style linkStyle = style.copy();
                    linkStyle.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
                    link.setStyle(linkStyle);
                    extras.add(link);

                    position = matcher.end();
                }

                Text previous = new LiteralText(message.substring(position));
                previous.setStyle(style);
                extras.add(previous);
                extras.addAll(extrasOld);

                for (Text current : extras) {
                    text.append(current);
                }
            }
        }

        List<Text> extras = component.getSiblings();
        for (int i = 0; i < extras.size(); i++) {
        	Text currentComponent = extras.get(i);
            if (currentComponent.getStyle() != null && currentComponent.getStyle().getClickEvent() == null) {
                extras.set(i, fixComponent(currentComponent, matcher));
            }
        }

        if (component instanceof TranslatableText) {
            Object[] parameters = ((TranslatableText) component).getArgs();
            for (int i = 0; i < parameters.length; i++) {
                Object currentParameter = parameters[i];
                if (currentParameter instanceof LiteralText) {
                	LiteralText componentParameter = (LiteralText) currentParameter;
                    if (componentParameter.getStyle() != null && componentParameter.getStyle().getClickEvent() == null) {
                        parameters[i] = fixComponent(componentParameter, matcher);
                    }
                } else if (currentParameter instanceof String && matcher.reset((String) currentParameter).find()) {
                    parameters[i] = fixComponent(new LiteralText((String) currentParameter), matcher);
                }
            }
        }

        return component;
    }

    private ChatUtilities() {
    }
}
