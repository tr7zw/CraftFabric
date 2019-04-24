package io.github.craftfabric.craftfabric.mixin.impl;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import net.minecraft.server.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleCommandMap.class)
public class SimpleCommandMapMixin {

    @Inject(at = @At("HEAD"), method = "register", remap = false)
    private void register(@NotNull String fallbackPrefix, @NotNull Command command, CallbackInfoReturnable<Boolean> info) {
        ((AbstractServerImpl) Bukkit.getServer()).getHandler().getCommandManager().getDispatcher().register(CommandManager.literal(command.getName())); //FIXME HORRIBLE tmp fix... FIXME
    }

}
