package io.github.craftfabric.craftfabric.mixin.impl.client;

import java.lang.reflect.Field;

import io.github.craftfabric.craftfabric.CraftFabric;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.IntegratedServerImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.LiteralText;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Inject(at = @At("HEAD"), method = "setupServer")
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
        Object server = this;
        new IntegratedServerImpl((IntegratedServer) server);
        ((AbstractServerImpl) Bukkit.getServer()).setupServer();
        MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new LiteralText("Loaded " + Bukkit.getPluginManager().getPlugins().length + " Bukkit Plugin(s)"), null));
    }
    
    @Inject(at = @At("RETURN"), method = "setupServer")
    private void setupServerFinished(CallbackInfoReturnable<Boolean> info) {
        ((AbstractServerImpl) Bukkit.getServer()).enablePlugins(PluginLoadOrder.POSTWORLD);
        MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new LiteralText("Enabled " + Bukkit.getPluginManager().getPlugins().length + " Bukkit Plugin(s)"), null));
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void shutdown(CallbackInfo info) {
        CraftFabric.LOGGER.info("FabricBukkit prepare-stopping!");
        ((AbstractServerImpl)Bukkit.getServer()).disablePlugins();
    }

    @Inject(at = @At("RETURN"), method = "shutdown")
    private void shutdownFinal(CallbackInfo info) {
        CraftFabric.LOGGER.info("FabricBukkit stopped!");
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
