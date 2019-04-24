package io.github.craftfabric.craftfabric.mixin.impl;

import com.mojang.authlib.GameProfile;
import io.github.craftfabric.craftfabric.CraftLink;
import io.netty.channel.local.LocalAddress;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(at = @At("RETURN"), method = "checkCanJoin")
    public TextComponent checkCanJoin(SocketAddress socketAddress_1, GameProfile gameProfile_1, CallbackInfoReturnable<TextComponent> info) {
        InetAddress address = null;
        if (socketAddress_1 instanceof InetSocketAddress) {
            address = ((InetSocketAddress) socketAddress_1).getAddress();
        } else if (socketAddress_1 instanceof LocalAddress) {
            address = new InetSocketAddress("localhost", 25565).getAddress(); // Maybe FIXME?
        }
        Bukkit.getPluginManager().callEvent(new AsyncPlayerPreLoginEvent(gameProfile_1.getName(), address, gameProfile_1.getId()));
        return null;
    }

    @SuppressWarnings("unchecked")
    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    public void onPlayerConnect(ClientConnection clientConnection_1, ServerPlayerEntity serverPlayerEntity_1, CallbackInfo info) {
        Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(((CraftLink<Player>) (Object) serverPlayerEntity_1).getCraftHandler(), "")); //FIXME message
    }

    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "remove")
    public void onQuit(ServerPlayerEntity serverPlayerEntity_1, CallbackInfo info) {
        Bukkit.getPluginManager().callEvent(new PlayerQuitEvent(((CraftLink<Player>) (Object) serverPlayerEntity_1).getCraftHandler(), "")); //FIXME message
    }

}
