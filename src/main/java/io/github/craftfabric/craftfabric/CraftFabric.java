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

    public static final Item FABRIC_ITEM = new Item(new Item.Settings().itemGroup(ItemGroup.MISC));
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("FabricBukkit is starting...");
        Registry.register(Registry.ITEM, new Identifier("my-mod", "fabric_item"), FABRIC_ITEM);

        LOG.info(Registry.BLOCK.get(1).getTranslationKey());

        Bukkit.getServer();
    }
}
