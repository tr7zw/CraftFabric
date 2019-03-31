package io.github.tr7zw.fabricbukkit.mixin.impl;

import com.mojang.authlib.GameProfile;
import io.github.tr7zw.fabricbukkit.craftfabric.CraftLink;
import io.github.tr7zw.fabricbukkit.craftfabric.entity.CraftPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.sortme.ChatMessageType;
import net.minecraft.text.TextComponent;
import net.minecraft.world.World;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer extends PlayerEntity implements CraftLink<Player> {

    @Shadow
    public ServerPlayNetworkHandler networkHandler;
    @Shadow
    @Final
    public MinecraftServer server;
    @Shadow
    @Final
    public ServerPlayerInteractionManager interactionManager;
    private CraftPlayer craftHandler;

    public MixinServerPlayer(World world_1, GameProfile gameProfile_1) { // Just needs to be here
        super(world_1, gameProfile_1);
    }

    //Testing code
    /*@Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo info) {
	System.out.println("Players: " + Bukkit.getOnlinePlayers().size() + " World:" + craftHandler.getWorld().getName());
    }*/

    @Shadow
    public abstract void sendChatMessage(TextComponent textComponent_1, ChatMessageType chatMessageType_1);

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onCreate(CallbackInfo info) {
        craftHandler = new CraftPlayer((ServerPlayerEntity) (Object) this);
    }

    @Override
    public Player getCraftHandler() {
        return craftHandler;
    }

}