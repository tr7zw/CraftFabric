package io.github.craftfabric.craftfabric.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.craftfabric.craftfabric.mixin.IWorldMixin;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class MixinWorld implements IWorldMixin {

	@Override
	public void updateWeather() {
		initWeatherGradients();
	}
    
	@Shadow
	abstract void initWeatherGradients();

}