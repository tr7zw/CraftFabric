package io.github.craftfabric.craftfabric.util;

import net.minecraft.util.Identifier;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class NamespaceUtils {

    private NamespaceUtils() {
    }

    public static NamespacedKey fromStringOrNull(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        Identifier minecraft = Identifier.create(string);
        return (minecraft == null) ? null : fromMinecraft(minecraft);
    }

    public static @NotNull NamespacedKey fromString(String string) {
        return fromMinecraft(new Identifier(string));
    }

    public static @NotNull NamespacedKey fromMinecraft(Identifier minecraft) {
        return new NamespacedKey(minecraft.getPath(), minecraft.getNamespace());
    }

    public static @NotNull Identifier toMinecraft(NamespacedKey key) {
        return new Identifier(key.getNamespace(), key.getKey());
    }
}
