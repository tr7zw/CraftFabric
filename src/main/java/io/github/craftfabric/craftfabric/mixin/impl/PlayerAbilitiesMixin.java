package io.github.craftfabric.craftfabric.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.craftfabric.craftfabric.mixin.IPlayerAbilitiesMixin;
import net.minecraft.entity.player.PlayerAbilities;

@Mixin(PlayerAbilities.class)
public class PlayerAbilitiesMixin implements IPlayerAbilitiesMixin{

	@Shadow
	boolean invulnerable;
	@Shadow
	boolean flying;
	@Shadow
	boolean allowFlying;
	@Shadow
	float flySpeed;
	@Shadow
	float walkSpeed;
	
	public boolean isInvulnerable() {
		return invulnerable;
	}
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}
	public boolean isFlying() {
		return flying;
	}
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	public boolean isAllowFlying() {
		return allowFlying;
	}
	public void setAllowFlying(boolean allowFlying) {
		this.allowFlying = allowFlying;
	}
	public float getFlySpeed() {
		return flySpeed;
	}
	public void setFlySpeed(float flySpeed) {
		this.flySpeed = flySpeed;
	}
	public float getWalkSpeed() {
		return walkSpeed;
	}
	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
	
}
