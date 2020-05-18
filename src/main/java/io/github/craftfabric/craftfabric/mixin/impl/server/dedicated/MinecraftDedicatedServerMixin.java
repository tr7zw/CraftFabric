package io.github.craftfabric.craftfabric.mixin.impl.server.dedicated;

import io.github.craftfabric.craftfabric.DedicatedServerImpl;
import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.mixin.impl.server.MinecraftServerMixin;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin extends MinecraftServerMixin {

    @Inject(at = @At("HEAD"), method = "setupServer")
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
        new DedicatedServerImpl((MinecraftDedicatedServer) (Object) this);
        ((AbstractServerImpl) Bukkit.getServer()).setupServer();
    }
    
    @Inject(at = @At("RETURN"), method = "setupServer")
    private void setupServerFinished(CallbackInfoReturnable<Boolean> info) {
        ((AbstractServerImpl) Bukkit.getServer()).enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void shutdown(CallbackInfo info) {
        System.out.println("FabricBukkit prepare-stopping!");
    }

    @Inject(at = @At("RETURN"), method = "shutdown")
    private void shutdownFinal(CallbackInfo info) {
        System.out.println("FabricBukkit stopped!");
    }

    @Inject(at = @At("HEAD"), method = "createGui", cancellable = true)
    public void createGui(CallbackInfo info) {
        info.cancel();
    }
}
