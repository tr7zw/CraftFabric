package io.github.craftfabric.craftfabric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftFabric implements ModInitializer {

    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("FabricBukkit loaded...");
    }
}
