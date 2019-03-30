package io.github.tr7zw.fabricbukkit.mixin.impl;

import com.mojang.authlib.GameProfile;
import io.github.tr7zw.fabricbukkit.craftfabric.CraftLink;
import io.github.tr7zw.fabricbukkit.craftfabric.CraftPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.config.BannedPlayerEntry;
import net.minecraft.server.config.WhitelistEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.network.packet.ChatMessageC2SPacket;
import net.minecraft.sortme.ChatMessageType;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

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
   /* @Inject(at = @At("RETURN"), method = "tick")
    public void tick(CallbackInfo info) {
	Collection<? extends Player> players = Bukkit.getOnlinePlayers();
	for(Player p : players)
	    System.out.println("OnlinePlayer: " + p.getName());
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

    //FIXME Nugget copy past for now, needs to be cleaned and moved to CraftPlayer

    public boolean isSpectator() {
        return interactionManager.getGameMode() == GameMode.SPECTATOR;
    }

    public boolean isCreative() {
        return this.interactionManager.getGameMode() == GameMode.CREATIVE;
    }

    public GameProfile getProfile() {
        return getGameProfile();
    }

    /**
     * Gets the socket address of this player
     *
     * @return the player's address
     */

    public InetSocketAddress getAddress() {
        return (InetSocketAddress) networkHandler.getConnection().getAddress();
    }

    /**
     * Kicks player with custom kick message.
     *
     * @param message kick message
     */

    public void kickPlayer(String message) {
        networkHandler.disconnect(new StringTextComponent(message));
    }

    /**
     * Says a message (or runs a command) as the player.
     *
     * @param msg message to print
     */

    public void chat(String msg) {
        networkHandler.onChatMessage(new ChatMessageC2SPacket(msg));
    }

    /**
     * Makes the player perform the given command
     *
     * @param command Command to perform
     * @return true if the command was successful, otherwise false
     */

    public boolean performCommand(String command) {
        return false;
    }

    /**
     * Returns if the player is in sneak mode
     *
     * @return true if player is in sneak mode
     */

    public boolean isSneaking() {
        return super.isSneaking();
    }

    /**
     * Sets the sneak mode the player
     *
     * @param sneak true if player should appear sneaking
     */

    public void setSneaking(boolean sneak) {
        super.setSneaking(sneak);
    }

    /**
     * Gets whether the player is sprinting or not.
     *
     * @return true if player is sprinting.
     */

    public boolean isSprinting() {
        return super.isSprinting();
    }

    /**
     * Sets whether the player is sprinting or not.
     *
     * @param sprinting true if the player should be sprinting
     */

    public void setSprinting(boolean sprinting) {
        super.setSprinting(sprinting);
    }

    /**
     * Gives the player the amount of experience specified.
     *
     * @param amount Exp amount to give
     */

    public void giveExp(int amount) {
        super.experience += amount;
    }

    /**
     * Gets the players current experience points towards the next level.
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @return Current experience points
     */

    public float getExp() {
        return super.experience;
    }

    /**
     * Sets the players current experience points towards the next level
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @param exp New experience points
     */

    public void setExp(float exp) {
        super.experienceBarProgress = exp;
    }

    /**
     * Gets the players current experience level
     *
     * @return Current experience level
     */

    public int getLevel() {
        return super.experienceLevel;
    }

    /**
     * Sets the players current experience level
     *
     * @param level New experience level
     */

    public void setLevel(int level) {
        super.experienceLevel = level;
    }

    /**
     * Gets the players total experience points
     *
     * @return Current total experience points
     */

    public int getTotalExperience() {
        return super.experience;
    }

    /**
     * Sets the players current experience level
     *
     * @param exp New experience level
     */

    public void setTotalExperience(int exp) {
        super.experience = exp;
    }

    /**
     * Gets the players current saturation level.
     * <p>
     * Saturation is a buffer for food level. Your food level will not drop if
     * you are saturated > 0.
     *
     * @return Saturation level
     */

    public float getSaturation() {
        return hungerManager.getSaturationLevel();
    }

    /**
     * Sets the players current saturation level
     *
     * @param value Saturation level
     */

    public void setSaturation(float value) {
        hungerManager.setSaturationLevelClient(value);
    }

    public String getPlayerName() {
        return super.getEntityName();
    }

    /**
     * Gets the players current food level
     *
     * @return Food level
     */

    public int getFoodLevel() {
        return hungerManager.getFoodLevel();
    }

    /**
     * Sets the players current food level
     *
     * @param value New food level
     */

    public void setFoodLevel(int value) {
        hungerManager.setFoodLevel(value);
    }

    /**
     * Determines if the Player is allowed to fly via jump key double-tap like
     * in creative mode.
     *
     * @return True if the player is allowed to fly.
     */

    public boolean getAllowFlight() {
        return this.abilities.allowFlying;
    }

    /**
     * Sets if the Player is allowed to fly via jump key double-tap like in
     * creative mode.
     *
     * @param flight If flight should be allowed.
     */

    public void setAllowFlight(boolean flight) {
        this.abilities.allowFlying = flight;
    }

    /**
     * Hides a player from this player
     *
     * @param player Player to hide
     */

    public void hidePlayer(Player player) {

    }

    /**
     * Allows this player to see a player that was previously hidden
     *
     * @param player Player to show
     */

    public void showPlayer(Player player) {

    }

    /**
     * Checks to see if a player has been hidden from this player
     *
     * @param player Player to check
     * @return True if the provided player is not being hidden from this
     * player
     */

    public boolean canSee(Player player) {
        ServerPlayerEntity entity = server.getPlayerManager().getPlayer(player.getUniqueId());
        if (entity == null)
            return false;
        return super.canSee(entity);
    }

    /**
     * Checks to see if this player is currently standing on a block. This
     * information may not be reliable, as it is a state provided by the
     * client, and may therefore not be accurate.
     *
     * @return True if the player standing on a solid block, else false.
     */

    public boolean isOnGround() {
        return super.onGround;
    }

    /**
     * Checks to see if this player is currently flying or not.
     *
     * @return True if the player is flying, else false.
     */

    public boolean isFlying() {
        return super.abilities.flying;
    }

    /**
     * Makes this player start or stop flying.
     *
     * @param value True to fly.
     */

    public void setFlying(boolean value) {
        super.abilities.flying = value;
    }

    /**
     * Sets the group of the player
     *
     * @param name the name of the group to set
     */
    public void setGroup(String name) {
        getProperties().put("group", name);
    }

    /**
     * Checks if this player is currently online
     *
     * @return true if they are online
     */

    public boolean isOnline() {
        return Objects.requireNonNull(super.getServer()).getPlayerManager().getPlayer(uuid) != null;
    }

    /**
     * Returns the UUID of this player
     *
     * @return Player UUID
     */

    public UUID getUniqueId() {
        return getGameProfile().getId();
    }

    /**
     * Checks if this player is banned or not
     *
     * @return true if banned, otherwise false
     */

    public boolean isBanned() {
        return server.getPlayerManager().getUserBanList().contains(getGameProfile()) || server.getPlayerManager().getIpBanList().contains(networkHandler.getConnection().getAddress());
    }

    /**
     * Bans or unbans this player
     *
     * @param banned true if banned
     */
    public void setBanned(boolean banned) {
        if (banned)
            server.getPlayerManager().getUserBanList().add(new BannedPlayerEntry(getGameProfile()));
        else
            server.getPlayerManager().getUserBanList().remove(getGameProfile());
    }

    /**
     * Checks if this player is whitelisted or not
     *
     * @return true if whitelisted
     */

    public boolean isWhitelisted() {
        return server.getPlayerManager().isWhitelisted(getGameProfile());
    }

    /**
     * Sets if this player is whitelisted or not
     *
     * @param value true if whitelisted
     */

    public void setWhitelisted(boolean value) {
        if (value)
            server.getPlayerManager().getWhitelist().add(new WhitelistEntry(getGameProfile()));
        else
            server.getPlayerManager().getWhitelist().remove(getGameProfile());
    }

    /**
     * Gets the first date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of first log-in for this player, or 0
     */

    public long getFirstPlayed() {
        return (long) getProperties().getOrDefault("firstPlayed", 0L);
    }

    /**
     * Gets the last date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of last log-in for this player, or 0
     */

    public long getLastPlayed() {
        return (long) getProperties().getOrDefault("lastPlayed", 0L);
    }

    /**
     * Checks if this player has played on this server before.
     *
     * @return True if the player has played before, otherwise false
     */

    public boolean hasPlayedBefore() {
        return (boolean) getProperties().getOrDefault("playedBefore", true);
    }

    /**
     * Gets the Location where the player will spawn at their bed, null if
     * they have not slept in one or their current bed spawn is invalid.
     *
     * @return Bed Spawn Location if bed exists, otherwise null.
     */

    public Location getBedSpawnLocation() {
        BlockPos sleepingPos = this.getSleepingPosition().orElse(getSpawnPosition());
        //FIXME
        return null;
        //return new Location(sleepingPos.getX(), sleepingPos.getY(), sleepingPos.getZ());
    }

    /**
     * Checks if this object is a server operator
     *
     * @return true if this is an operator, otherwise false
     */

    public boolean isOp() {
        return server.getPlayerManager().isOperator(getGameProfile());
    }

    /**
     * Sets the operator status of this object
     *
     * @param value New operator value
     */

    public void setOp(boolean value) {
        if (value)
            server.getPlayerManager().addToOperators(getGameProfile());
        else
            server.getPlayerManager().removeFromOperators(getGameProfile());
    }

    public HashMap<String, Object> getProperties() {
        return null; //FIXME
        //return Nugget.getPlayerData(getUniqueId());
    }

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */

    public void sendMessage(String message) {
        sendMessage(new StringTextComponent(message));
    }


    @SuppressWarnings("unchecked")
    public boolean hasPermission(String name) {
        if (isOp())
            return true;
        //FIXME
        return true;
        //return Nugget.getPermissionManager().hasPlayerPermission(this, name);
    }

    public void sendMessage(TextComponent message) {
        sendChatMessage(message, ChatMessageType.SYSTEM);
    }


    public Location getLocation() {
        return null; //FIXME
        //return new Location(getPos().getX(), getPos().getY(), getPos().getZ());
    }
}