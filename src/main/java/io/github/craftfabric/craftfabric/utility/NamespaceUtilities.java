package io.github.craftfabric.craftfabric.utility;

import net.minecraft.util.Identifier;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class NamespaceUtilities {

    public static NamespacedKey stringToNMSOrNull(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        Identifier minecraft = Identifier.tryParse(string);
        return (minecraft == null) ? null : fromNMS(minecraft);
    }

    public static @NotNull NamespacedKey stringToNMS(String string) {
        return fromNMS(new Identifier(string));
    }

    public static @NotNull NamespacedKey fromNMS(Identifier identifier) {
        return new NamespacedKey(identifier.getPath(), identifier.getNamespace());
    }

    public static @NotNull Identifier toNMS(NamespacedKey key) {
        return new Identifier(key.getNamespace(), key.getKey());
    }

    private NamespaceUtilities() {
    }
}
