package io.github.craftfabric.craftfabric.mixin.impl.server.network;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.entity.CraftPlayer;
import io.github.craftfabric.craftfabric.mixin.impl.entity.LivingEntityMixin;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.Text;
import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends LivingEntityMixin<CraftPlayer> {

    @Shadow
    public ServerPlayNetworkHandler networkHandler;
    @Shadow
    @Final
    public MinecraftServer server;
    @Shadow
    @Final
    public ServerPlayerInteractionManager interactionManager;

    //Testing code
    @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo info) {
        //System.out.println("Items: " + craftHandler.getInventory().contains(Material.STONE));
    }

    @Shadow
    public abstract void sendChatMessage(Text textComponent_1, MessageType chatMessageType_1);

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onCreate(CallbackInfo info) {
        craftHandler = new CraftPlayer((AbstractServerImpl) Bukkit.getServer(), (ServerPlayerEntity) (Object) this);
    }

    @Override
    public CraftPlayer getCraftHandler() {
        return (CraftPlayer) craftHandler;
    }
}
