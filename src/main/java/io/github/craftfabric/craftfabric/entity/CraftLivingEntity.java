package io.github.craftfabric.craftfabric.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.EntityEffect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.block.TargetBlockInfo.FluidMode;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import com.google.common.collect.Sets;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.link.CraftLink;
import net.minecraft.entity.damage.DamageSource;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {

    protected final net.minecraft.entity.LivingEntity handle;

    public CraftLivingEntity(final AbstractServerImpl server, net.minecraft.entity.LivingEntity entity) {
        super(server, entity);
        handle = entity;
    }

    public net.minecraft.entity.LivingEntity getHandle() {
        return handle;
    }

    public void setHandle(final net.minecraft.entity.LivingEntity entity) {
        super.setHandle(entity);
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void damage(double amount) {
        getHandle().damage(DamageSource.GENERIC, (float) amount);
    }

    @Override
    public void damage(double amount, Entity source) {
        if (!(source instanceof LivingEntity)) {
            throw new IllegalArgumentException("Damage source Entity has to be living!");
        }
        getHandle().damage(DamageSource.mob(((CraftLivingEntity) source).getHandle()), (float) amount);
    }

    @Override
    public double getHealth() {
        return getHandle().getHealth();
    }

    @Override
    public void setHealth(double health) {
        getHandle().setHealth((float) health);
    }

    @Override
    public double getMaxHealth() {
        return getHandle().getMaximumHealth();
    }

    @Override
    public void setMaxHealth(double health) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetMaxHealth() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getEntityId() {
        return getHandle().getEntityId();
    }

    @Override
    public int getFireTicks() {
        // TODO Auto-generated method stub
        // FIXME will need mixin
        return 0;
    }

    @Override
    public void setFireTicks(int ticks) {
        getHandle().setOnFireFor(ticks);
    }

    @Override
    public int getMaxFireTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void remove() {
        getHandle().remove();
    }

    @Override
    public boolean isDead() {
        return !getHandle().isAlive();
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public boolean isPersistent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public Entity getPassenger() {
        return ((CraftLink<Entity>) getHandle().getPrimaryPassenger()).getCraftHandler();
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        // TODO Auto-generated method stub
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Entity> getPassengers() {
        return getHandle().getPassengerList().stream().map(passanger -> ((CraftLink<Entity>) passanger).getCraftHandler()).collect(Collectors.toList());
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean eject() {
        boolean wasRiding = getHandle().getVehicle() != null;
        getHandle().stopRiding();
        return wasRiding;
    }

    @Override
    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    @Override
    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public UUID getUniqueId() {
        return getHandle().getUuid();
    }

    @Override
    public int getTicksLived() {
        return getHandle().age;
    }

    @Override
    public void setTicksLived(int value) {
        getHandle().age = value;
    }

    @Override
    public void playEffect(EntityEffect type) {
        // TODO Auto-generated method stub

    }

    @Override
    public EntityType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isInsideVehicle() {
        return getHandle().hasVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return eject();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entity getVehicle() {
        if (!isInsideVehicle()) return null;
        return ((CraftLink<Entity>) getHandle().getVehicle()).getCraftHandler();
    }

    @Override
    public boolean isCustomNameVisible() {
        return getHandle().isCustomNameVisible();
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getHandle().setCustomNameVisible(flag);
    }

    @Override
    public boolean isGlowing() {
        return getHandle().isGlowing();
    }

    @Override
    public void setGlowing(boolean flag) {
        getHandle().setGlowing(flag);
    }

    @Override
    public boolean isInvulnerable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setInvulnerable(boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSilent() {
        return getHandle().isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        getHandle().setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setGravity(boolean gravity) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getPortalCooldown() {
        return getHandle().netherPortalCooldown;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getHandle().netherPortalCooldown = cooldown;
    }

    @Override
    public Set<String> getScoreboardTags() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockFace getFacing() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Spigot spigot() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] messages) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getName() {
        return getHandle().getName().asFormattedString();
    }

    @Override
    public boolean isPermissionSet(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasPermission(String name) {
        System.out.println("Hasperm: " + isOp());
        return isOp(); // FIXME perms
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return isOp(); // FIXME perms
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        // TODO Auto-generated method stub

    }

    @Override
    public void recalculatePermissions() {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isOp() {
        return getHandle().allowsPermissionLevel(2); // 2 seems to be OP
    }

    @Override
    public void setOp(boolean value) {
        // TODO
    }

    @Override
    public String getCustomName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) return getHandle().getStandingEyeHeight();
        return getHandle().getEyeHeight(getHandle().getPose());
    }

    @Override
    public Location getEyeLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
    	return getLineOfSight(transparent, maxDistance, 0);
    }
    
    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        if (transparent == null) {
            transparent = Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
        }
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (!transparent.contains(material)) {
                break;
            }
        }
        return blocks;
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
    	List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
    	return getLineOfSight(transparent, maxDistance, 2);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance) {
    	return this.getTargetBlockExact(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
    	RayTraceResult hitResult = this.rayTraceBlocks(maxDistance, fluidCollisionMode);
        return (hitResult != null ? hitResult.getHitBlock() : null);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
    	return this.rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
    	Location eyeLocation = this.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        return this.getWorld().rayTraceBlocks(eyeLocation, direction, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public int getRemainingAir() {
        return getHandle().getAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        getHandle().setAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return getHandle().getMaxAir();
    }

    @Override
    public void setMaximumAir(int ticks) {
        throw new UnsupportedOperationException("Currently not implemented!");
    }

    @Override
    public int getMaximumNoDamageTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getLastDamage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getNoDamageTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        // TODO Auto-generated method stub

    }

    @Override
    public Player getKiller() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        // TODO Auto-generated method stub

    }

    @Override
    public EntityEquipment getEquipment() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getCanPickupItems() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isLeashed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGliding() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setGliding(boolean gliding) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSwimming() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSwimming(boolean swimming) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isRiptiding() {
        return getHandle().isUsingRiptide();
    }

    @Override
    public void setAI(boolean ai) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasAI() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCollidable() {
        // TODO
    	return true;
    }

    @Override
    public void setCollidable(boolean collidable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRotation(float yaw, float pitch) {
        Location location = getLocation();
        location.setYaw(yaw);
        location.setPitch(pitch);
        teleport(location);
    }

	@Override
	public Pose getPose() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersistentDataContainer getPersistentDataContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSleeping() {
		return getHandle().isSleeping();
	}

	@Override
	public <T> T getMemory(MemoryKey<T> memoryKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setMemory(MemoryKey<T> memoryKey, T memoryValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getAbsorptionAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAbsorptionAmount(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @Nullable Location getOrigin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fromMobSpawner() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public @NotNull Chunk getChunk() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull SpawnReason getEntitySpawnReason() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Block getTargetBlock(int maxDistance, @NotNull FluidMode fluidMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable BlockFace getTargetBlockFace(int maxDistance, @NotNull FluidMode fluidMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable TargetBlockInfo getTargetBlockInfo(int maxDistance, @NotNull FluidMode fluidMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Entity getTargetEntity(int maxDistance, boolean ignoreBlocks) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable TargetEntityInfo getTargetEntityInfo(int maxDistance, boolean ignoreBlocks) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKiller(@Nullable Player killer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getArrowsStuck() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setArrowsStuck(int arrows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getShieldBlockingDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setShieldBlockingDelay(int delay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @Nullable ItemStack getActiveItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getItemUseRemainingTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHandRaisedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isHandRaised() {
		// TODO Auto-generated method stub
		return false;
	}

}
