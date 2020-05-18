package io.github.craftfabric.craftfabric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class CraftFabric implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("FabricBukkit loaded...");
        //Enchantment.isAcceptingRegistrations();
        // TODO: Hook into fabric api so we can:
        // Filter registries for new enchantments other mods have already added.
        // Hook into "RegistryEntryAddedCallback" so we can automatically register new enchantments added by other mods
        // Use either ServerStartCallback or an equivalent hook in the client to lock registrations.
        // TODO: Similar thing with Potions
        //PotionEffectType.values();
        // May require some additional work since potions use numerical ids in bukkit, but registry identifiers in nms
    }
}
