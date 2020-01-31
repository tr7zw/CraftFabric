package io.github.craftfabric.craftfabric.inventory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import com.destroystokyo.paper.Namespaced;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonParseException;

import io.github.craftfabric.craftfabric.CraftMagicNumbers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class CraftMetaItem implements ItemMeta, Damageable, Repairable {

    static final ItemMetaKey NAME = new ItemMetaKey("Name", "display-name");
    static final ItemMetaKey LOCNAME = new ItemMetaKey("LocName", "loc-name");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey DISPLAY = new ItemMetaKey("display");
    static final ItemMetaKey LORE = new ItemMetaKey("Lore", "lore");
    static final ItemMetaKey ENCHANTMENTS = new ItemMetaKey("Enchantments", "enchants");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_ID = new ItemMetaKey("id");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
    static final ItemMetaKey REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
    static final ItemMetaKey ATTRIBUTES = new ItemMetaKey("AttributeModifiers", "attribute-modifiers");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_NAME = new ItemMetaKey("Name");
    // @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_VALUE = new ItemMetaKey("Amount");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_TYPE = new ItemMetaKey("Operation");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_HIGH = new ItemMetaKey("UUIDMost");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_LOW = new ItemMetaKey("UUIDLeast");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_SLOT = new ItemMetaKey("Slot");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("HideFlags", "ItemFlags");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey UNBREAKABLE = new ItemMetaKey("Unbreakable");
    //@Specific(Specific.To.NBT)
    static final ItemMetaKey DAMAGE = new ItemMetaKey("Damage");
    static final ItemMetaKey BUKKIT_CUSTOM_TAG = new ItemMetaKey("PublicBukkitValues");
    private static final Set<String> HANDLED_TAGS = Sets.newHashSet();
    private Text displayName;
    private Text locName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;
    private int repairCost;
    private int hideFlag;
    private boolean unbreakable;
    private int damage;
    private CompoundTag internalTag;
    // private static final CraftCustomTagTypeRegistry TAG_TYPE_REGISTRY = new CraftCustomTagTypeRegistry();

    CraftMetaItem(CompoundTag tag) {
        if (tag.contains(DISPLAY.NBT)) {
            CompoundTag display = tag.getCompound(DISPLAY.NBT);

            if (display.contains(NAME.NBT)) {
                try {
                    displayName = Text.Serializer.fromJson(display.getString(NAME.NBT));
                } catch (JsonParseException ex) {
                    // Ignore (stripped like Vanilla)
                }
            }

            if (display.contains(LOCNAME.NBT)) {
                try {
                    locName = Text.Serializer.fromJson(display.getString(LOCNAME.NBT));
                } catch (JsonParseException ex) {
                    // Ignore (stripped like Vanilla)
                }
            }

            if (display.contains(LORE.NBT)) {
                ListTag list = display.getList(LORE.NBT, CraftMagicNumbers.NBT.TAG_STRING);
                lore = new ArrayList<String>(list.size());

                for (int index = 0; index < list.size(); index++) {
                    String line = list.getString(index);
                    lore.add(line);
                }
            }
        }

		/*this.enchantments = buildEnchantments(tag, ENCHANTMENTS);
        this.attributeModifiers = buildModifiers(tag, ATTRIBUTES);*/ //FIXME

        if (tag.contains(REPAIR.NBT)) {
            repairCost = tag.getInt(REPAIR.NBT);
        }

        if (tag.contains(HIDEFLAGS.NBT)) {
            hideFlag = tag.getInt(HIDEFLAGS.NBT);
        }
        if (tag.contains(UNBREAKABLE.NBT)) {
            unbreakable = tag.getBoolean(UNBREAKABLE.NBT);
        }
        if (tag.contains(DAMAGE.NBT)) {
            damage = tag.getInt(DAMAGE.NBT);
        }
        if (tag.contains(BUKKIT_CUSTOM_TAG.NBT)) {
            CompoundTag compound = tag.getCompound(BUKKIT_CUSTOM_TAG.NBT);
            Set<String> keys = compound.getKeys();
            for (String key : keys) {
                //FIXME publicItemTagContainer.put(key, compound.get(key));
            }
        }

        Set<String> keys = tag.getKeys();
        for (String key : keys) {
            // if (!getHandledTags().contains(key)) {
            //FIXME unhandledTags.put(key, tag.get(key));
            // }
        }
    }
    //  private final Map<String, NBTBase> unhandledTags = new HashMap<String, NBTBase>();
    // private final CraftCustomItemTagContainer publicItemTagContainer = new CraftCustomItemTagContainer(TAG_TYPE_REGISTRY);

    @Override
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasDisplayName() {
        return displayName != null;
    }

    @Override
    public String getDisplayName() {
        return displayName.asFormattedString();
    }

    @Override
    public void setDisplayName(String name) {
        displayName = new LiteralText(name);
    }

    @Override
    public boolean hasLocalizedName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getLocalizedName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLocalizedName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasLore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public boolean hasRepairCost() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getRepairCost() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRepairCost(int cost) {
        // TODO Auto-generated method stub

    }

    @Override
    public CraftMetaItem clone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasDamage() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getDamage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setDamage(int damage) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasEnchants() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasEnchant(Enchantment ench) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getEnchantLevel(Enchantment ench) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeEnchant(Enchantment ench) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasConflictingEnchant(Enchantment ench) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void addItemFlags(ItemFlag... itemFlags) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeItemFlags(ItemFlag... itemFlags) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUnbreakable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasAttributeModifiers() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttributeModifiers(Multimap<Attribute, AttributeModifier> attributeModifiers) {
        // TODO Auto-generated method stub

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers(Attribute attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAttributeModifier(Attribute attribute) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAttributeModifier(EquipmentSlot slot) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public CustomItemTagContainer getCustomTagContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    static class ItemMetaKey {

        final String BUKKIT;
        final String NBT;

        ItemMetaKey(final String both) {
            this(both, both);
        }

        ItemMetaKey(final String nbt, final String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.FIELD)
        @interface Specific {
            To value();

            enum To {
                BUKKIT,
                NBT,
                ;
            }
        }
    }

	@Override
	public PersistentDataContainer getPersistentDataContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomModelData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCustomModelData() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCustomModelData(Integer data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Material> getCanDestroy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCanDestroy(Set<Material> canDestroy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Material> getCanPlaceOn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCanPlaceOn(Set<Material> canPlaceOn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @NotNull Set<Namespaced> getDestroyableKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDestroyableKeys(@NotNull Collection<Namespaced> canDestroy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @NotNull Set<Namespaced> getPlaceableKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlaceableKeys(@NotNull Collection<Namespaced> canPlaceOn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasPlaceableKeys() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasDestroyableKeys() {
		// TODO Auto-generated method stub
		return false;
	}


}
