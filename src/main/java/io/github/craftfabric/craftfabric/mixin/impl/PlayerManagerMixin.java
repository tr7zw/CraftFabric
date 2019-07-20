package io.github.craftfabric.craftfabric.mixin.impl;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import io.github.craftfabric.craftfabric.CraftLink;
import io.netty.channel.local.LocalAddress;
import net.minecraft.client.render.model.CubeFace;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ThreadExecutor;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

	private ExecutorService asyncLoginExecutor = Executors.newFixedThreadPool(4);
	
    @Inject(at = @At("HEAD"), method = "checkCanJoin", cancellable=true)
    public Text checkCanJoin(SocketAddress socketAddress_1, GameProfile gameProfile_1, CallbackInfoReturnable<Text> info) {
        InetAddress address = null;
        if (socketAddress_1 instanceof InetSocketAddress) {
            address = ((InetSocketAddress) socketAddress_1).getAddress();
        } else if (socketAddress_1 instanceof LocalAddress) {
            address = new InetSocketAddress("localhost", 25565).getAddress(); // Maybe FIXME?
        }
        final InetAddress fAddress = address;
        Future<AsyncPlayerPreLoginEvent> futureEvent = asyncLoginExecutor.submit(new Callable<AsyncPlayerPreLoginEvent>() {

			@Override
			public AsyncPlayerPreLoginEvent call() throws Exception {
				AsyncPlayerPreLoginEvent event = new AsyncPlayerPreLoginEvent(gameProfile_1.getName(), fAddress, gameProfile_1.getId());
				Bukkit.getPluginManager().callEvent(event);
				return event;
			}
		});
        
        try {
        	AsyncPlayerPreLoginEvent event = futureEvent.get(10, TimeUnit.SECONDS);
        	if(event.getLoginResult() == Result.ALLOWED) {
        		return null;
        	} else {
        		info.setReturnValue(new LiteralText(event.getKickMessage()));
        	}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return new LiteralText("Internal Server error!");
		}
        
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
