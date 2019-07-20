package io.github.craftfabric.craftfabric.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import io.github.craftfabric.craftfabric.CraftMagicNumbers;
import net.minecraft.item.Item;

public class CraftItemStack extends ItemStack {

    net.minecraft.item.ItemStack handle;

    /**
     * Mirror
     */
    private CraftItemStack(net.minecraft.item.ItemStack item) {
        this.handle = item;
    }

    private CraftItemStack(ItemStack item) {
        this(item.getType(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
    }

    private CraftItemStack(Material type, int amount, short durability, ItemMeta itemMeta) {
        setType(type);
        setAmount(amount);
        setDurability(durability);
        setItemMeta(itemMeta);
    }

    public static net.minecraft.item.ItemStack asNMSCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return stack.handle == null ? net.minecraft.item.ItemStack.EMPTY : stack.handle.copy();
        }
        if (original == null || original.getType() == Material.AIR) {
            return net.minecraft.item.ItemStack.EMPTY;
        }

        Item item = CraftMagicNumbers.getItem(original.getType());

        if (item == null) {
            return net.minecraft.item.ItemStack.EMPTY;
        }

        net.minecraft.item.ItemStack stack = new net.minecraft.item.ItemStack(item, original.getAmount());
        if (original.hasItemMeta()) {
            setItemMeta(stack, original.getItemMeta());
        } else {
            // Converted after setItemMeta
            //FIXME? stack.convertStack();
        }
        return stack;
    }

    public static net.minecraft.item.ItemStack copyNMSStack(net.minecraft.item.ItemStack original, int amount) {
        net.minecraft.item.ItemStack stack = original.copy();
        stack.setCount(amount);
        return stack;
    }

    /**
     * Copies the NMS stack to return as a strictly-Bukkit stack
     */
    public static ItemStack asBukkitCopy(net.minecraft.item.ItemStack original) {
        if (original.isEmpty()) {
            return new ItemStack(Material.AIR);
        }
        ItemStack stack = new ItemStack(CraftMagicNumbers.getMaterial(original.getItem()), original.getCount());
        if (hasItemMeta(original)) {
            stack.setItemMeta(getItemMeta(original));
        }
        return stack;
    }

    public static CraftItemStack asCraftMirror(net.minecraft.item.ItemStack original) {
        return new CraftItemStack((original == null || original.isEmpty()) ? null : original);
    }

    public static CraftItemStack asCraftCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return new CraftItemStack(stack.handle == null ? null : stack.handle.copy());
        }
        return new CraftItemStack(original);
    }

    public static CraftItemStack asNewCraftStack(Item item) {
        return asNewCraftStack(item, 1);
    }

    public static CraftItemStack asNewCraftStack(Item item, int amount) {
        return new CraftItemStack(CraftMagicNumbers.getMaterial(item), amount, (short) 0, null);
    }

    static boolean hasItemMeta(net.minecraft.item.ItemStack item) {
        return !(item == null || item.getTag() == null || item.getTag().isEmpty());
    }

    public static ItemMeta getItemMeta(net.minecraft.item.ItemStack item) {
        if (!hasItemMeta(item)) {
            // FIXME return CraftItemFactory.instance().getItemMeta(getType(item));
        }
        switch (getType(item)) {
	           /* case WRITTEN_BOOK:
	                return new CraftMetaBookSigned(item.getTag());
	            case WRITABLE_BOOK:
	                return new CraftMetaBook(item.getTag());
	            case CREEPER_HEAD:
	            case CREEPER_WALL_HEAD:
	            case DRAGON_HEAD:
	            case DRAGON_WALL_HEAD:
	            case PLAYER_HEAD:
	            case PLAYER_WALL_HEAD:
	            case SKELETON_SKULL:
	            case SKELETON_WALL_SKULL:
	            case WITHER_SKELETON_SKULL:
	            case WITHER_SKELETON_WALL_SKULL:
	            case ZOMBIE_HEAD:
	            case ZOMBIE_WALL_HEAD:
	                return new CraftMetaSkull(item.getTag());
	            case LEATHER_HELMET:
	            case LEATHER_CHESTPLATE:
	            case LEATHER_LEGGINGS:
	            case LEATHER_BOOTS:
	                return new CraftMetaLeatherArmor(item.getTag());
	            case POTION:
	            case SPLASH_POTION:
	            case LINGERING_POTION:
	            case TIPPED_ARROW:
	                return new CraftMetaPotion(item.getTag());
	            case FILLED_MAP:
	                return new CraftMetaMap(item.getTag());
	            case FIREWORK_ROCKET:
	                return new CraftMetaFirework(item.getTag());
	            case FIREWORK_STAR:
	                return new CraftMetaCharge(item.getTag());
	            case ENCHANTED_BOOK:
	                return new CraftMetaEnchantedBook(item.getTag());
	            case BLACK_BANNER:
	            case BLACK_WALL_BANNER:
	            case BLUE_BANNER:
	            case BLUE_WALL_BANNER:
	            case BROWN_BANNER:
	            case BROWN_WALL_BANNER:
	            case CYAN_BANNER:
	            case CYAN_WALL_BANNER:
	            case GRAY_BANNER:
	            case GRAY_WALL_BANNER:
	            case GREEN_BANNER:
	            case GREEN_WALL_BANNER:
	            case LIGHT_BLUE_BANNER:
	            case LIGHT_BLUE_WALL_BANNER:
	            case LIGHT_GRAY_BANNER:
	            case LIGHT_GRAY_WALL_BANNER:
	            case LIME_BANNER:
	            case LIME_WALL_BANNER:
	            case MAGENTA_BANNER:
	            case MAGENTA_WALL_BANNER:
	            case ORANGE_BANNER:
	            case ORANGE_WALL_BANNER:
	            case PINK_BANNER:
	            case PINK_WALL_BANNER:
	            case PURPLE_BANNER:
	            case PURPLE_WALL_BANNER:
	            case RED_BANNER:
	            case RED_WALL_BANNER:
	            case WHITE_BANNER:
	            case WHITE_WALL_BANNER:
	            case YELLOW_BANNER:
	            case YELLOW_WALL_BANNER:
	                return new CraftMetaBanner(item.getTag());
	            case BAT_SPAWN_EGG:
	            case BLAZE_SPAWN_EGG:
	            case CAVE_SPIDER_SPAWN_EGG:
	            case CHICKEN_SPAWN_EGG:
	            case COD_SPAWN_EGG:
	            case COW_SPAWN_EGG:
	            case CREEPER_SPAWN_EGG:
	            case DOLPHIN_SPAWN_EGG:
	            case DROWNED_SPAWN_EGG:
	            case DONKEY_SPAWN_EGG:
	            case ELDER_GUARDIAN_SPAWN_EGG:
	            case ENDERMAN_SPAWN_EGG:
	            case ENDERMITE_SPAWN_EGG:
	            case EVOKER_SPAWN_EGG:
	            case GHAST_SPAWN_EGG:
	            case GUARDIAN_SPAWN_EGG:
	            case HORSE_SPAWN_EGG:
	            case HUSK_SPAWN_EGG:
	            case LLAMA_SPAWN_EGG:
	            case MAGMA_CUBE_SPAWN_EGG:
	            case MOOSHROOM_SPAWN_EGG:
	            case MULE_SPAWN_EGG:
	            case OCELOT_SPAWN_EGG:
	            case PARROT_SPAWN_EGG:
	            case PHANTOM_SPAWN_EGG:
	            case PIG_SPAWN_EGG:
	            case POLAR_BEAR_SPAWN_EGG:
	            case PUFFERFISH_SPAWN_EGG:
	            case RABBIT_SPAWN_EGG:
	            case SALMON_SPAWN_EGG:
	            case SHEEP_SPAWN_EGG:
	            case SHULKER_SPAWN_EGG:
	            case SILVERFISH_SPAWN_EGG:
	            case SKELETON_HORSE_SPAWN_EGG:
	            case SKELETON_SPAWN_EGG:
	            case SLIME_SPAWN_EGG:
	            case SPIDER_SPAWN_EGG:
	            case SQUID_SPAWN_EGG:
	            case STRAY_SPAWN_EGG:
	            case TROPICAL_FISH_SPAWN_EGG:
	            case TURTLE_SPAWN_EGG:
	            case VEX_SPAWN_EGG:
	            case VILLAGER_SPAWN_EGG:
	            case VINDICATOR_SPAWN_EGG:
	            case WITCH_SPAWN_EGG:
	            case WITHER_SKELETON_SPAWN_EGG:
	            case WOLF_SPAWN_EGG:
	            case ZOMBIE_HORSE_SPAWN_EGG:
	            case ZOMBIE_PIGMAN_SPAWN_EGG:
	            case ZOMBIE_SPAWN_EGG:
	            case ZOMBIE_VILLAGER_SPAWN_EGG:
	                return new CraftMetaSpawnEgg(item.getTag());
	            case KNOWLEDGE_BOOK:
	                return new CraftMetaKnowledgeBook(item.getTag());
	            case FURNACE:
	            case CHEST:
	            case TRAPPED_CHEST:
	            case JUKEBOX:
	            case DISPENSER:
	            case DROPPER:
	            case SIGN:
	            case SPAWNER:
	            case BREWING_STAND:
	            case ENCHANTING_TABLE:
	            case COMMAND_BLOCK:
	            case REPEATING_COMMAND_BLOCK:
	            case CHAIN_COMMAND_BLOCK:
	            case BEACON:
	            case DAYLIGHT_DETECTOR:
	            case HOPPER:
	            case COMPARATOR:
	            case SHIELD:
	            case STRUCTURE_BLOCK:
	            case SHULKER_BOX:
	            case WHITE_SHULKER_BOX:
	            case ORANGE_SHULKER_BOX:
	            case MAGENTA_SHULKER_BOX:
	            case LIGHT_BLUE_SHULKER_BOX:
	            case YELLOW_SHULKER_BOX:
	            case LIME_SHULKER_BOX:
	            case PINK_SHULKER_BOX:
	            case GRAY_SHULKER_BOX:
	            case LIGHT_GRAY_SHULKER_BOX:
	            case CYAN_SHULKER_BOX:
	            case PURPLE_SHULKER_BOX:
	            case BLUE_SHULKER_BOX:
	            case BROWN_SHULKER_BOX:
	            case GREEN_SHULKER_BOX:
	            case RED_SHULKER_BOX:
	            case BLACK_SHULKER_BOX:
	            case ENDER_CHEST:
	                return new CraftMetaBlockState(item.getTag(), CraftMagicNumbers.getMaterial(item.getItem()));
	            case TROPICAL_FISH_BUCKET:
	                return new CraftMetaTropicalFishBucket(item.getTag());*/
            default:
                return new CraftMetaItem(item.getTag());
        }
    }

    static Material getType(net.minecraft.item.ItemStack item) {
        return item == null ? Material.AIR : CraftMagicNumbers.getMaterial(item.getItem());
    }

    public static boolean setItemMeta(net.minecraft.item.ItemStack item, ItemMeta itemMeta) {
        //TODO

        return true;
    }

    @Override
    public MaterialData getData() {
        throw new UnsupportedOperationException("Material Data has been removed!");
    }

    @Override
    public Material getType() {
        return handle != null ? CraftMagicNumbers.getMaterial(handle.getItem()) : Material.AIR;
    }

    @Override
    public void setType(Material type) {
        if (getType() == type) {
            return;
        } else if (type == Material.AIR) {
            handle = null;
        } else if (CraftMagicNumbers.getItem(type) == null) { // :(
            handle = null;
        } else if (handle == null) {
            handle = new net.minecraft.item.ItemStack(CraftMagicNumbers.getItem(type), 1);
        } else {
            net.minecraft.item.ItemStack replacement = new net.minecraft.item.ItemStack(CraftMagicNumbers.getItem(type), handle.getCount());
            if (hasItemMeta()) {
                // This will create the appropriate item meta, which will contain all the data we intend to keep
                ItemMeta meta = getItemMeta(handle);
                handle = replacement;
                setItemMeta(handle, meta);
            } else {
                handle = replacement;
            }
        }
        setData(null);
    }

    @Override
    public int getAmount() {
        return handle != null ? handle.getCount() : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (handle == null) {
            return;
        }

        handle.setCount(amount);
        if (amount == 0) {
            handle = null;
        }
    }

    @Override
    public short getDurability() {
        if (handle != null) {
            return (short) handle.getDamage();
        } else {
            return -1;
        }
    }

    @Override
    public void setDurability(final short durability) {
        // Ignore damage if item is null
        if (handle != null) {
            handle.setDamage(durability);
        }
    }

    @Override
    public int getMaxStackSize() {
        return (handle == null) ? Material.AIR.getMaxStackSize() : handle.getItem().getMaxCount();
    }

    @Override
    public boolean hasItemMeta() {
        return hasItemMeta(handle); // FIXME? && !CraftItemFactory.instance().equals(getItemMeta(), null);
    }

    @Override
    public ItemMeta getItemMeta() {
        return getItemMeta(handle);
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        return setItemMeta(handle, itemMeta);
    }

}
