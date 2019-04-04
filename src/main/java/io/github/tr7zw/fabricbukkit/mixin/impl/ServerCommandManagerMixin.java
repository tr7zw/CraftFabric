package io.github.tr7zw.fabricbukkit.mixin.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.tr7zw.fabricbukkit.craftfabric.CraftLink;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerCommandManager.class)
public class ServerCommandManagerMixin {

	@SuppressWarnings("unchecked")
	@Inject(at = @At("HEAD"), method = "execute", cancellable = true)
	public int execute(ServerCommandSource serverCommandSource_1, String string_1, CallbackInfoReturnable<Integer> info) {
		System.out.println("Command and stuff " + serverCommandSource_1 + " " + string_1);
		try {
			ServerPlayerEntity player = serverCommandSource_1.getPlayer();
			System.out.println("Player: " + player);
			if(player != null) {
				boolean worked = Bukkit.getServer().dispatchCommand(((CraftLink<Player>)player).getCraftHandler(), string_1);
					info.setReturnValue(0);
					info.cancel();
					return 0;
			}
		}catch(CommandSyntaxException ex) {}
		return 0;
	}

}
