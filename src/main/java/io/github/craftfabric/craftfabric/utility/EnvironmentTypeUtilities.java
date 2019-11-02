package io.github.craftfabric.craftfabric.utility;

import org.bukkit.World.Environment;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.dimension.DimensionType;

public class EnvironmentTypeUtilities {

    @NotNull
    public static Environment fromNMS(@NotNull DimensionType type) {
    	switch (type.getRawId()) {
            case 0:
                return Environment.NORMAL;
            case -1:
                return Environment.NETHER;
            case 1:
                return Environment.THE_END;
            default:
                throw new IllegalArgumentException("Invalid DimensionType!");
        }
    }

    @NotNull
    public static DimensionType toNMS(@NotNull Environment type) {
        switch (type) {
            case NORMAL:
                return DimensionType.OVERWORLD;
            case NETHER:
                return DimensionType.THE_NETHER;
            case THE_END:
                return DimensionType.THE_END;
            default:
                throw new IllegalArgumentException("Invalid DimensionType!");
        }
    }
	
}
