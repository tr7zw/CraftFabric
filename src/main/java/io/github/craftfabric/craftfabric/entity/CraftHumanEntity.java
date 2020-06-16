package io.github.craftfabric.craftfabric.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.github.craftfabric.craftfabric.accessor.entity.player.ItemCooldownManagerAccessor;
import io.github.craftfabric.craftfabric.accessor.entity.player.PlayerEntityAccessor;
import io.github.craftfabric.craftfabric.accessor.server.network.ServerPlayerEntityAccessor;
import org.bukkit.GameMode;
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
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Preconditions;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.CraftMagicNumbers;
import io.github.craftfabric.craftfabric.inventory.CraftInventoryPlayer;
import io.github.craftfabric.craftfabric.inventory.CraftItemStack;
import io.github.craftfabric.craftfabric.utility.NamespaceUtilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Arm;

public abstract class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    //private CraftInventoryPlayer inventory;
    //private final CraftInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;
    private final CraftInventoryPlayer inventory;

    public CraftHumanEntity(final AbstractServerImpl server, final net.minecraft.entity.player.PlayerEntity entity) {
        super(server, entity);
        mode = getServer().getDefaultGameMode();
        op = getHandle().allowsPermissionLevel(2);
        perm.recalculatePermissions();
        inventory = new CraftInventoryPlayer(getHandle().inventory, this);
        //this.inventory = new CraftInventoryPlayer(entity.inventory);
        //enderChest = new CraftInventory(entity.getEnderChest());
    }

    @Override
    public @NotNull PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public @NotNull Inventory getEnderChest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull MainHand getMainHand() {
        if (getHandle().getMainArm() == Arm.LEFT) {
            return MainHand.LEFT;
        } else {
            return MainHand.RIGHT;
        }
    }

    @Override
    public @NotNull ItemStack getItemInHand() {
        return inventory.getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        inventory.setItemInHand(item);
    }

    @Override
    public @NotNull ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(getHandle().inventory.getCursorStack());
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        getHandle().inventory.setCursorStack(stack);
        if (this instanceof CraftPlayer) {
            ((ServerPlayerEntityAccessor) getHandle()).invokeUpdateCursorStack();
        }
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
    public boolean sleep(@NotNull Location location, boolean force) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        // TODO Auto-generated method stub

    }

    @Override
    public @NotNull Location getBedLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull String getName() {
        return getHandle().getName().asFormattedString();
    }

    @Override
    public boolean isOp() {
        return getHandle().allowsPermissionLevel(2);
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        return perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
    public void setOp(boolean value) {
        op = true;
        perm.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @Override
    public @NotNull GameMode getGameMode() {
        return mode;
    }

    @Override
    public void setGameMode(@NotNull GameMode mode) {
        this.mode = Objects.requireNonNull(mode, "Mode cannot be null");
    }

    @Override
    public PlayerEntity getHandle() {
        return (PlayerEntity) handle;
    }

    public void setHandle(final PlayerEntity entity) {
        super.setHandle(entity);
        //this.inventory = new CraftInventoryPlayer(entity.inventory);
    }

    @Override
    public @NotNull InventoryView getOpenInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openInventory(@NotNull Inventory inventory) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void openInventory(@NotNull InventoryView inventory) {
        // TODO Auto-generated method stub

    }

    @Override
    public InventoryView openMerchant(@NotNull Villager trader, boolean force) {
        Objects.requireNonNull(trader, "trader cannot be null");
        return this.openMerchant((Merchant) trader, force);
    }

    @Override
    public InventoryView openMerchant(@NotNull Merchant merchant, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void closeInventory() {
        ((PlayerEntityAccessor) getHandle()).invokeCloseContainer();
    }

    @Override
    public boolean isBlocking() {
        return getHandle().isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return getHandle().isUsingItem();
    }

    @Override
    public boolean setWindowProperty(@NotNull Property prop, int value) {
        return false;
    }

    @Override
    public int getExpToLevel() {
        return 0; // TODO Implement me
        //return ((IPlayerEntityMixin) getHandle()).getExpToLevel();
    }

    @Override
    public boolean hasCooldown(@NotNull Material material) {
        Objects.requireNonNull(material, "material");
        return getHandle().getItemCooldownManager().isCoolingDown(CraftMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(@NotNull Material material) {
        Objects.requireNonNull(material, "material");
        return (int) getHandle().getItemCooldownManager().getCooldownProgress(CraftMagicNumbers.getItem(material), ((ItemCooldownManagerAccessor) getHandle().getItemCooldownManager()).getTicks());
    }

    @Override
    public void setCooldown(@NotNull Material material, int ticks) {
        Objects.requireNonNull(material, "material");
        Preconditions.checkArgument(ticks >= 0, "Cannot have negative cooldown");
        getHandle().getItemCooldownManager().set(CraftMagicNumbers.getItem(material), ticks);
    }

    @Override
    public boolean discoverRecipe(@NotNull NamespacedKey recipe) {
        return discoverRecipes(Collections.singletonList(recipe)) != 0;
    }

    @Override
    public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        return getHandle().unlockRecipes(bukkitKeysToMinecraftRecipes(recipes));
    }

    @Override
    public boolean undiscoverRecipe(@NotNull NamespacedKey recipe) {
        return undiscoverRecipes(Collections.singletonList(recipe)) != 0;
    }

    @Override
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        return getHandle().lockRecipes(bukkitKeysToMinecraftRecipes(recipes));
    }

    private Collection<net.minecraft.recipe.Recipe<?>> bukkitKeysToMinecraftRecipes(Collection<NamespacedKey> recipeKeys) {
        Collection<net.minecraft.recipe.Recipe<?>> recipes = new ArrayList<>();
        RecipeManager manager = server.getHandler().getRecipeManager();

        for (NamespacedKey recipeKey : recipeKeys) {
            Optional<? extends Recipe<?>> recipe = manager.get(NamespaceUtilities.toNMS(recipeKey));
            if (!recipe.isPresent()) {
                continue;
            }
            recipes.add(recipe.get());
        }

        return recipes;
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
