package io.github.tr7zw.fabricbukkit.craftfabric.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
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
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import io.github.tr7zw.fabricbukkit.craftfabric.CraftLink;
import net.minecraft.entity.damage.DamageSource;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {

	private final net.minecraft.entity.LivingEntity handler;

	public CraftLivingEntity(net.minecraft.entity.LivingEntity entity) {
		super(entity);
		handler = entity;
	}

	public net.minecraft.entity.LivingEntity getHandler() {
		return handler;
	}

	@Override
	public AttributeInstance getAttribute(Attribute attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void damage(double amount) {
		getHandler().damage(DamageSource.GENERIC, (float) amount);
	}

	@Override
	public void damage(double amount, Entity source) {
		if(!(source instanceof LivingEntity)) {
			throw new IllegalArgumentException("Damage source Entity has to be living!");
		}
		getHandler().damage(DamageSource.mob(((CraftLivingEntity)source).getHandler()), (float) amount);
	}

	@Override
	public double getHealth() {
		return getHandler().getHealth();
	}

	@Override
	public void setHealth(double health) {
		getHandler().setHealth((float) health);
	}

	@Override
	public double getMaxHealth() {
		return getHandler().getHealthMaximum();
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
		return getHandler().getEntityId();
	}

	@Override
	public int getFireTicks() {
		// TODO Auto-generated method stub
		// FIXME will need mixin
		return 0;
	}

	@Override
	public void setFireTicks(int ticks) {
		getHandler().setOnFireFor(ticks);
	}

	@Override
	public int getMaxFireTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void remove() {
		getHandler().remove();
	}

	@Override
	public boolean isDead() {
		return !getHandler().isAlive();
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
		return ((CraftLink<Entity>) getHandler().getPrimaryPassenger()).getCraftHandler();
	}

	@Override
	public boolean setPassenger(Entity passenger) {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entity> getPassengers() {
		return getHandler().getPassengerList().stream().map(passanger -> ((CraftLink<Entity>)passanger).getCraftHandler()).collect(Collectors.toList());
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
		boolean wasRiding = getHandler().getRiddenEntity() != null;
		getHandler().stopRiding();
		return wasRiding;
	}

	@Override
	public float getFallDistance() {
		return getHandler().fallDistance;
	}

	@Override
	public void setFallDistance(float distance) {
		getHandler().fallDistance = distance;
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
		return getHandler().getUuid();
	}

	@Override
	public int getTicksLived() {
		return getHandler().age;
	}

	@Override
	public void setTicksLived(int value) {
		getHandler().age = value;
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
		return getHandler().hasVehicle();
	}

	@Override
	public boolean leaveVehicle() {
		return eject();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entity getVehicle() {
		if(!isInsideVehicle())return null;
		return ((CraftLink<Entity>) getHandler().getRiddenEntity()).getCraftHandler();
	}

	@Override
	public boolean isCustomNameVisible() {
		return getHandler().isCustomNameVisible();
	}

	@Override
	public void setCustomNameVisible(boolean flag) {
		getHandler().setCustomNameVisible(flag);
	}

	@Override
	public boolean isGlowing() {
		return getHandler().isGlowing();
	}

	@Override
	public void setGlowing(boolean flag) {
		getHandler().setGlowing(flag);
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
		return getHandler().isSilent();
	}

	@Override
	public void setSilent(boolean flag) {
		getHandler().setSilent(flag);
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
		return getHandler().portalCooldown;
	}

	@Override
	public void setPortalCooldown(int cooldown) {
		getHandler().portalCooldown = cooldown;
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
		return getHandler().getName().getFormattedText();
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
		return isOp(); // FIXME perms
	}

	@Override
	public boolean hasPermission(Permission perm) {
		// TODO Auto-generated method stub
		return false;
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
		return getHandler().allowsPermissionLevel(2); // 2 seems to be OP
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
		if (ignorePose) return getHandler().getStandingEyeHeight();
		return getHandler().getEyeHeight(getHandler().getPose());
	}

	@Override
	public Location getEyeLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getTargetBlockExact(int maxDistance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RayTraceResult rayTraceBlocks(double maxDistance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemainingAir() {
		return getHandler().getBreath();
	}

	@Override
	public void setRemainingAir(int ticks) {
		getHandler().setBreath(ticks);
	}

	@Override
	public int getMaximumAir() {
		return getHandler().getMaxBreath();
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
		return getHandler().isUsingRiptide();
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
		return getHandler().isPushable();
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

}
