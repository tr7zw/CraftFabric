package io.github.craftfabric.craftfabric.utility;

import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

public final class GameModeUtilities {

    @NotNull
    public static GameMode fromNMS(@NotNull net.minecraft.world.GameMode gameMode) {
        switch (gameMode) {
            case SURVIVAL:
                return GameMode.SURVIVAL;
            case CREATIVE:
                return GameMode.CREATIVE;
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SPECTATOR:
                return GameMode.SPECTATOR;
            default:
                throw new IllegalArgumentException("Invalid GameMode state!");
        }
    }

    @NotNull
    public static net.minecraft.world.GameMode toNMS(@NotNull GameMode gameMode) {
        switch (gameMode) {
            case SURVIVAL:
                return net.minecraft.world.GameMode.SURVIVAL;
            case CREATIVE:
                return net.minecraft.world.GameMode.CREATIVE;
            case ADVENTURE:
                return net.minecraft.world.GameMode.ADVENTURE;
            case SPECTATOR:
                return net.minecraft.world.GameMode.SPECTATOR;
            default:
                throw new IllegalArgumentException("Invalid GameMode state!");
        }
    }

    private GameModeUtilities() {
    }
}
