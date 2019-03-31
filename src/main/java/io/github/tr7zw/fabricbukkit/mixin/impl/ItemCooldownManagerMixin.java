package io.github.tr7zw.fabricbukkit.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.tr7zw.fabricbukkit.mixin.IItemCooldownManagerMixin;
import net.minecraft.entity.player.ItemCooldownManager;

@Mixin(ItemCooldownManager.class)
public class ItemCooldownManagerMixin implements IItemCooldownManagerMixin{

	@Shadow
	private int tick;

	@Override
	public int getTick() {
		return tick;
	}
	
}
