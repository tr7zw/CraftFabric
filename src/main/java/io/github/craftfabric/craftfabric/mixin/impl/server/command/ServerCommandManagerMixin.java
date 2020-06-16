package io.github.craftfabric.craftfabric.mixin.impl.server.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.link.CraftLink;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandManager.class)
public abstract class ServerCommandManagerMixin {

    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "execute", cancellable = true)
    private void execute(ServerCommandSource serverCommandSource, String input, CallbackInfoReturnable<Integer> info) {
        try {
            ServerPlayerEntity player = serverCommandSource.getPlayer();
            if (player != null) {
                String bukkitCommand = input;
                if (bukkitCommand.startsWith("/")) {
                    bukkitCommand = bukkitCommand.substring(1);
                }
                // fixme: Definitely not correct, command source isn't always a player
                boolean worked = ((AbstractServerImpl) Bukkit.getServer()).getCommandMap().dispatch(((CraftLink<Player>) player).getCraftHandler(), bukkitCommand);
                if (worked) {
                    info.setReturnValue(0);
                }
            }
        } catch (CommandSyntaxException ex) {
        }

        // fixme: Is always cancelling needed?
        //return 0;
    }

}
