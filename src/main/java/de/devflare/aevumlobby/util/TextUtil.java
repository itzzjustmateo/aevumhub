package de.devflare.aevumlobby.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.stream.Collectors;

public final class TextUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    private TextUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Component parse(final String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        try {
            return MINI_MESSAGE.deserialize(text);
        } catch (final Exception exception) {
            return LEGACY_SERIALIZER.deserialize(text);
        }
    }

    public static List<Component> parse(final List<String> lines) {
        return lines.stream()
                .map(TextUtil::parse)
                .collect(Collectors.toList());
    }

    public static String replace(final String text, final String placeholder, final String replacement) {
        if (text == null || placeholder == null || replacement == null) {
            return text;
        }
        return text.replace(placeholder, replacement);
    }

    public static String replacePlaceholders(String text, final String... replacements) {
        if (text == null || replacements == null || replacements.length % 2 != 0) {
            return text;
        }

        for (int i = 0; i < replacements.length; i += 2) {
            text = text.replace(replacements[i], replacements[i + 1]);
        }

        return text;
    }

    public static String stripFormatting(final String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("(?i)&[0-9A-FK-OR]", "")
                .replaceAll("<[^>]+>", "");
    }
}
