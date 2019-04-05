package io.github.tr7zw.fabricbukkit.mixin.impl.client;

import io.github.tr7zw.fabricbukkit.craftfabric.AbstractServerImpl;
import io.github.tr7zw.fabricbukkit.craftfabric.IntegratedServerImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.StringTextComponent;
import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(at = @At("HEAD"), method = "setupServer")
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
        Object server = this;
        new IntegratedServerImpl((IntegratedServer) server);
        ((AbstractServerImpl) Bukkit.getServer()).setupServer();
        MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new StringTextComponent("Loaded " + Bukkit.getPluginManager().getPlugins().length + " Bukkit Plugin(s)"), null));
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void shutdown(CallbackInfo info) {
        System.out.println("FabricBukkit prepare-stopping!");
    }

    @Inject(at = @At("RETURN"), method = "shutdown")
    private void shutdownFinal(CallbackInfo info) {
        System.out.println("FabricBukkit stopped!");
        try {
            Field f = Bukkit.class.getDeclaredField("server");
            f.setAccessible(true);
            f.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
