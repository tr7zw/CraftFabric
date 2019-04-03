package io.github.tr7zw.fabricbukkit.craftfabric.entity;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;

import com.google.common.base.Preconditions;

import io.github.tr7zw.fabricbukkit.craftfabric.CraftMagicNumbers;
import io.github.tr7zw.fabricbukkit.craftfabric.inventory.CraftItemStack;
import io.github.tr7zw.fabricbukkit.mixin.IItemCooldownManagerMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.AbsoluteHand;

public abstract class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {

	private final PlayerEntity handler;

    public CraftHumanEntity(net.minecraft.entity.player.PlayerEntity entity) {
        super(entity);
        this.handler = entity;
    }

    public PlayerEntity getHandle() {
        return handler;
    }

    @Override
    public PlayerInventory getInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Inventory getEnderChest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MainHand getMainHand() {
        if (getHandle().getMainHand() == AbsoluteHand.LEFT) {
            return MainHand.LEFT;
        } else {
            return MainHand.RIGHT;
        }
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InventoryView getOpenInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void openInventory(InventoryView inventory) {
        // TODO Auto-generated method stub

    }

    @Override
    public InventoryView openMerchant(Villager trader, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack getItemInHand() {
        return CraftItemStack.asBukkitCopy(getHandle().getMainHandStack());
    }

    @Override
    public void setItemInHand(ItemStack item) {
        // TODO Auto-generated method stub

    }

    @Override
    public ItemStack getItemOnCursor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasCooldown(Material material) {
        return getHandle().getItemCooldownManager().isCooldown(CraftMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");

        return (int) getHandle().getItemCooldownManager().getCooldownProgress(CraftMagicNumbers.getItem(material), ((IItemCooldownManagerMixin) (Object) getHandle().getItemCooldownManager()).getTick());
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        getHandle().getItemCooldownManager().set(CraftMagicNumbers.getItem(material), ticks);
    }

    @Override
    public boolean isSleeping() {
        return getHandle().isSleeping();
    }

    @Override
    public int getSleepTicks() {
        return getHandle().getSleepTimer();
    }

    @Override
    public Location getBedSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean sleep(Location location, boolean force) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getBedLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isBlocking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHandRaised() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getExpToLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Entity getShoulderEntityLeft() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setShoulderEntityLeft(Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public Entity getShoulderEntityRight() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setShoulderEntityRight(Entity entity) {
        // TODO Auto-generated method stub

    }
}
