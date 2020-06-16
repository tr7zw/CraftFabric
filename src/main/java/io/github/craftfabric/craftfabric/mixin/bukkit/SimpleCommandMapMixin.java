package io.github.craftfabric.craftfabric.mixin.bukkit;

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

@Mixin(value = SimpleCommandMap.class, remap = false)
public abstract class SimpleCommandMapMixin {

    @Inject(at = @At("HEAD"), method = "register(Ljava/lang/String;Lorg/bukkit/command/Command;)Z", remap = false)
    private void register(@NotNull String fallbackPrefix, @NotNull Command command, CallbackInfoReturnable<Boolean> info) {
        ((AbstractServerImpl) Bukkit.getServer()).getHandler().getCommandManager().getDispatcher().register(CommandManager.literal(command.getName())); //FIXME HORRIBLE tmp fix... FIXME
    }

}
