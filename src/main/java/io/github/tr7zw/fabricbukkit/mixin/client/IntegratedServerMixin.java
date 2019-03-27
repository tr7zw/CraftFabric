package io.github.tr7zw.fabricbukkit.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.integrated.IntegratedServer;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(at = @At("HEAD"), method = "setupServer")
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
	System.out.println("FabricBukkit starting up!");
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
