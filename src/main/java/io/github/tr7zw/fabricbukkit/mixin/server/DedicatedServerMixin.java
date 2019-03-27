package io.github.tr7zw.fabricbukkit.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;

@Mixin(MinecraftDedicatedServer.class)
public class DedicatedServerMixin {

    @Inject(at = @At("HEAD"), method = "setupServer")
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
	System.out.println("FabricBukkit starting up!");
    }

}
