package io.github.craftfabric.craftfabric.mixin.impl.world;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.craftfabric.craftfabric.mixin.IWorldMixin;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin implements IWorldMixin {

	@Override
	public void updateWeather() {
		initWeatherGradients();
	}
    
	@Shadow
	abstract void initWeatherGradients();

}
