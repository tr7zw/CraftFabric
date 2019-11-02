package io.github.craftfabric.craftfabric.mixin;

public interface IPlayerAbilitiesMixin {

	public boolean isInvulnerable();
	public void setInvulnerable(boolean invulnerable);
	public boolean isFlying();
	public void setFlying(boolean flying);
	public boolean isAllowFlying();
	public void setAllowFlying(boolean allowFlying);
	public float getFlySpeed();
	public void setFlySpeed(float flySpeed);
	public float getWalkSpeed();
	public void setWalkSpeed(float walkSpeed);
	
}
