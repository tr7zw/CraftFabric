package io.github.craftfabric.craftfabric.mixin.impl;

import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.craftfabric.craftfabric.scheduler.CraftScheduler;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Shadow
	private int ticks;
	
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
    	((CraftScheduler)Bukkit.getServer().getScheduler()).mainThreadHeartbeat(ticks + 1);
    }

}
