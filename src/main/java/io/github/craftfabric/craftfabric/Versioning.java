package io.github.craftfabric.craftfabric;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Versioning {

    private Versioning() {
    }

    @NotNull
    public static String getBukkitVersion() {
        String result = "Unknown-Version";

        try (InputStream stream = Bukkit.class.getClassLoader().getResourceAsStream("META-INF/maven/org.spigotmc/spigot-api/pom.properties")) {
            if (stream != null) {
                Properties properties = new Properties();
                properties.load(stream);
                result = properties.getProperty("version");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
