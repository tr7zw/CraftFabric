package io.github.tr7zw.fabricbukkit.mixin.impl;

import org.spongepowered.asm.mixin.Shadow;

import io.github.tr7zw.fabricbukkit.mixin.IItemCooldownManagerMixin;

public class ItemCooldownManagerMixin implements IItemCooldownManagerMixin{

	@Shadow
	private int tick;

	@Override
	public int getTick() {
		return tick;
	}
	
}
