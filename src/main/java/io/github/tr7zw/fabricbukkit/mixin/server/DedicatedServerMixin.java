package io.github.tr7zw.fabricbukkit.mixin.server;

import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.tr7zw.fabricbukkit.craftfabric.ServerImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

@Mixin(MinecraftDedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(at = @At("HEAD"), method = "setupServer")
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
	Object server = this;
	Bukkit.setServer(new ServerImpl((MinecraftServer) server));
	((ServerImpl) Bukkit.getServer()).setupServer();
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void shutdown(CallbackInfo info) {
	System.out.println("FabricBukkit prepare-stopping!");
    }

    @Inject(at = @At("RETURN"), method = "shutdown")
    private void shutdownFinal(CallbackInfo info) {
	System.out.println("FabricBukkit stopped!");
    }

}
