package io.github.craftfabric.craftfabric.mixin.impl.server.network;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.craftfabric.craftfabric.link.CraftLink;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractionType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {

	@Shadow
	public ClientConnection connection;
	@Shadow
	private MinecraftServer server;
	@Shadow
	public ServerPlayerEntity player;
	
	@SuppressWarnings("unchecked")
	public Player getPlayer() {
		return ((CraftLink<Player>) player).getCraftHandler();
	}
	
	@Shadow
	public void sendPacket(Packet<?> packet_1) {
		
	}

	@SuppressWarnings("unchecked")
	@Inject(at = @At("HEAD"), method = "onPlayerInteractEntity", cancellable = true)
	public void onPlayerInteractEntity(PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket_1,
			CallbackInfo info) {
		NetworkThreadUtils.forceMainThread(playerInteractEntityC2SPacket_1, (ServerPlayPacketListener)this, this.player.getServerWorld());
		ServerWorld serverWorld_1 = this.server.getWorld(this.player.dimension);
		Entity entity_1 = playerInteractEntityC2SPacket_1.getEntity(serverWorld_1);
		if (entity_1 != null) {
			if(!(entity_1 instanceof CraftLink<?>)) {
				System.out.println("Unable to create InteractEvent for unlinked Entity '" + entity_1.getClass().getName());
				return;
			}
			boolean boolean_1 = this.player.canSee(entity_1);
	         double double_1 = 36.0D;
	         if (!boolean_1) {
	            double_1 = 9.0D;
	         }

	         if (this.player.squaredDistanceTo(entity_1) < double_1) {
				if (playerInteractEntityC2SPacket_1.getType() == InteractionType.INTERACT || playerInteractEntityC2SPacket_1.getType() == InteractionType.INTERACT_AT) {
					
					Item origItem = this.player.inventory.getMainHandStack() == null ? null : this.player.inventory.getMainHandStack().getItem();
					PlayerInteractEntityEvent event;
	                if (playerInteractEntityC2SPacket_1.getType() == InteractionType.INTERACT) {
	                   event = new PlayerInteractEntityEvent(this.getPlayer(), ((CraftLink<? extends org.bukkit.entity.Entity>) entity_1).getCraftHandler(), playerInteractEntityC2SPacket_1.getHand() == Hand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
	                } else {
	                	Vec3d target = playerInteractEntityC2SPacket_1.getHitPosition();
	                   event = new PlayerInteractAtEntityEvent(this.getPlayer(), ((CraftLink<? extends org.bukkit.entity.Entity>) entity_1).getCraftHandler(), new Vector(target.x, target.y, target.z), playerInteractEntityC2SPacket_1.getHand() == Hand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
	                }
	                
                    Bukkit.getPluginManager().callEvent((Event)event);
                    if (entity_1 instanceof FishEntity && origItem != null && origItem == Items.WATER_BUCKET && (event.isCancelled() || this.player.inventory.getMainHandStack() == null || this.player.inventory.getMainHandStack().getItem() != origItem)) {
                       this.sendPacket(new EntitySpawnS2CPacket((FishEntity)entity_1));
                       //this.player.updateInventory(this.player.playerContainer); //TODO
                    }

                    //TODO
                    //if (triggerLeashUpdate && (((PlayerInteractEntityEvent)event).isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().getItem() != origItem)) {
                    //    this.sendPacket(new PacketPlayOutAttachEntity(entity, ((EntityInsentient)entity).getLeashHolder()));
                    // }

                    if (((PlayerInteractEntityEvent)event).isCancelled() || this.player.inventory.getMainHandStack() == null || this.player.inventory.getMainHandStack().getItem() != origItem) {
                       this.sendPacket(new EntityTrackerUpdateS2CPacket(entity_1.getEntityId(), entity_1.getDataTracker(), true));
                    }

                    if (((PlayerInteractEntityEvent)event).isCancelled()) {
                       return;
                    }

	                
				}
			}	
		}
	}

}
