package io.github.tr7zw.fabricbukkit.craftfabric;

import io.github.tr7zw.fabricbukkit.craftfabric.util.NamespaceUtils;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("deprecation")
public final class CraftMagicNumbers implements UnsafeValues {
    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    // Mappings
    private static final Map<Block, Material> BLOCK_MATERIAL = new HashMap<>();
    private static final Map<Material, Block> MATERIAL_BLOCK = new HashMap<>();

    static {
        for (Block block : Registry.BLOCK) {
            BLOCK_MATERIAL.put(block, Material.getMaterial(Registry.BLOCK.getId(block).getPath().toUpperCase(Locale.ROOT)));
        }
        for (Material material : Material.values()) {
            Identifier key = key(material);
            MATERIAL_BLOCK.put(material, Registry.BLOCK.get(key));
        }
    }

    private CraftMagicNumbers() {
    }

    public static @NotNull Material getMaterial(Block block) {
        return BLOCK_MATERIAL.get(block);
    }

    public static @NotNull Block getBlock(Material material) {
        return MATERIAL_BLOCK.get(material);
    }

    public static @NotNull Identifier key(Material mat) {
        if (mat.isLegacy()) {
            throw new IllegalArgumentException("Legacy materials aren't supported!");
        }

        return NamespaceUtils.toMinecraft(mat.getKey());
    }

    @Override
    public Material toLegacy(Material material) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Material fromLegacy(Material material) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getDataVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {
        // Nah
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        return clazz; //no processing
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * This helper class represents the different NBT Tags.
     * <p>
     * These should match NBTBase#getTypeId
     */
    public static class NBT {

        public static final int TAG_END = 0;
        public static final int TAG_BYTE = 1;
        public static final int TAG_SHORT = 2;
        public static final int TAG_INT = 3;
        public static final int TAG_LONG = 4;
        public static final int TAG_FLOAT = 5;
        public static final int TAG_DOUBLE = 6;
        public static final int TAG_BYTE_ARRAY = 7;
        public static final int TAG_STRING = 8;
        public static final int TAG_LIST = 9;
        public static final int TAG_COMPOUND = 10;
        public static final int TAG_INT_ARRAY = 11;
        public static final int TAG_ANY_NUMBER = 99;
    }
}
