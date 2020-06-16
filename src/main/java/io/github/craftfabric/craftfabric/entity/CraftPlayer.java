package io.github.craftfabric.craftfabric.entity;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.github.craftfabric.craftfabric.accessor.entity.player.PlayerAbilitiesAccessor;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.command.ConversationTracker;
import io.github.craftfabric.craftfabric.scoreboard.CraftScoreboardManager;
import io.github.craftfabric.craftfabric.utility.ChatUtilities;
import io.github.craftfabric.craftfabric.utility.GameModeUtilities;
import io.github.craftfabric.craftfabric.utility.NamespaceUtilities;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.WhitelistEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class CraftPlayer extends CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;

    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<>();

    private int hash = 0;

    public CraftPlayer(AbstractServerImpl server, ServerPlayerEntity handler) {
        super(server, handler);
        firstPlayed = System.currentTimeMillis();
    }

    public ServerPlayerEntity getHandle() {
        return (ServerPlayerEntity) handle;
    }

    public GameProfile getProfile() {
        return getHandle().getGameProfile();
    }

    public void addChannel(String channel) {
        Preconditions.checkState(channels.size() < 128, "Cannot register channel '%s'. Too many channels registered!", channel);
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.add(channel)) {
            server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.remove(channel)) {
            server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    private Collection<Recipe<?>> bukkitKeysToMinecraftRecipes(Collection<NamespacedKey> recipeKeys) {
        Collection<Recipe<?>> recipes = new ArrayList<>();
        RecipeManager manager = server.getHandler().getRecipeManager();

        for (NamespacedKey recipeKey : recipeKeys) {
            Optional<? extends Recipe<?>> recipe = manager.get(NamespaceUtilities.toNMS(recipeKey));
            if (!recipe.isPresent()) {
                continue;
            }

            recipes.add(recipe.get());
        }

        return recipes;
    }

    @Override
    public @Nullable InventoryView openWorkbench(@Nullable Location location, boolean force) {
        // TODO
        return null;
    }

    @Override
    public void closeInventory() {
        getHandle().closeContainer();
    }

    @Override
    public @NotNull GameMode getGameMode() {
        return GameModeUtilities.fromNMS(getHandle().interactionManager.getGameMode());
    }

    @Override
    public void setGameMode(@NotNull GameMode mode) {
        getHandle().setGameMode(GameModeUtilities.toNMS(mode));
    }

    @Override
    public void sendMessage(@NotNull String message) {
        getHandle().sendChatMessage(new LiteralText(message), MessageType.SYSTEM);
    }

    @Override
    public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        return getHandle().getRecipeBook().unlockRecipes(bukkitKeysToMinecraftRecipes(recipes), getHandle());
    }

    @Override
    public boolean discoverRecipe(@NotNull NamespacedKey recipe) {
        return undiscoverRecipes(Collections.singletonList(recipe)) != 0;
    }

    @Override
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        return getHandle().getRecipeBook().lockRecipes(bukkitKeysToMinecraftRecipes(recipes), getHandle());
    }

    @Override
    public boolean undiscoverRecipe(@NotNull NamespacedKey recipe) {
        return 1 == getHandle().getRecipeBook().lockRecipes(bukkitKeysToMinecraftRecipes(Collections.singleton(recipe)), getHandle());
    }

    @Override
    public boolean isConversing() {
        return conversationTracker.isConversing();
    }

    @Override
    public void acceptConversationInput(@NotNull String input) {
        conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public boolean isOnline() {
        return server.getPlayer(getUniqueId()) != null;
    }

    @Override
    public boolean isBanned() {
        return server.getHandler().getPlayerManager().getUserBanList().contains(getProfile());
    }

    @Override
    public boolean isWhitelisted() {
        return server.getHandler().getPlayerManager().isWhitelisted(getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandler().getPlayerManager().getWhitelist().add(new WhitelistEntry(getProfile()));
        } else {
            server.getHandler().getPlayerManager().getWhitelist().remove(getProfile());
        }
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    @Override
    public long getFirstPlayed() {
        return firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", getName());
        return result;
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        if (getHandle().networkHandler == null) {
            return;
        }
        if (channels.contains(channel)) {
            channel = StandardMessenger.validateAndCorrectChannel(channel);
            CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(new Identifier(channel), new PacketByteBuf(Unpooled.wrappedBuffer(message)));
            getHandle().networkHandler.sendPacket(packet);
        }
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    @Override
    public @NotNull String getDisplayName() {
        return getHandle().getDisplayName().asFormattedString();
    }

    @Override
    public void setDisplayName(String name) {
        // TODO Kinda hard to vanilla implement?
    }

    @Override
    public @NotNull String getPlayerListName() {
        return Team.modifyText(getHandle().getScoreboardTeam(), getHandle().getName()).asFormattedString();
    }

    @Override
    public void setPlayerListName(String name) {
        // TODO Kinda hard to vanilla implement?
    }

    @Override
    public String getPlayerListHeader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPlayerListHeader(String header) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPlayerListFooter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPlayerListFooter(String footer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        // TODO Auto-generated method stub

    }

    @Override
    public @NotNull Location getCompassTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCompassTarget(@NotNull Location loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) getHandle().networkHandler.getConnection().getAddress();
    }

    @Override
    public void sendRawMessage(@NotNull String message) {
        if (getHandle().networkHandler == null) {
            return;
        }
        for (Text component : ChatUtilities.fromString(message)) {
            getHandle().networkHandler.sendPacket(new ChatMessageS2CPacket(component));
        }
    }

    @Override
    public void kickPlayer(String message) {
        getHandle().networkHandler.disconnect(new LiteralText(message));
    }

    @Override
    public void chat(@NotNull String msg) {
        getHandle().networkHandler.onChatMessage(new ChatMessageC2SPacket(msg));
    }

    @Override
    public boolean performCommand(@NotNull String command) {
        return Bukkit.getServer().dispatchCommand(this, command);
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isSneaking();
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setSneaking(true);
    }

    @Override
    public boolean isSprinting() {
        return getHandle().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getHandle().setSprinting(sprinting);
    }

    @Override
    public void saveData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadData() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isSleepingIgnored() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playNote(@NotNull Location loc, byte instrument, byte note) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(@NotNull String sound) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(@NotNull Sound sound, SoundCategory category) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(@NotNull String sound, SoundCategory category) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playEffect(@NotNull Location loc, @NotNull Effect effect, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void playEffect(@NotNull Location loc, @NotNull Effect effect, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull Material material, byte data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull BlockData block) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean sendChunkChange(@NotNull Location loc, int sx, int sy, int sz, @NotNull byte[] data) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendSignChange(@NotNull Location loc, String[] lines) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMap(@NotNull MapView map) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        getHandle().getStatHandler().increaseStat(getHandle(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), 1);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        getHandle().getStatHandler().increaseStat(getHandle(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), -1);
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        getHandle().getStatHandler().increaseStat(getHandle(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), amount);
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        getHandle().getStatHandler().increaseStat(getHandle(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), -1 * amount);
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {
        getHandle().getStatHandler().setStat(getHandle(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), newValue);
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        return getHandle().getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())));
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getPlayerTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getPlayerTimeOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetPlayerTime() {
        // TODO Auto-generated method stub

    }

    @Override
    public WeatherType getPlayerWeather() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPlayerWeather(@NotNull WeatherType type) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetPlayerWeather() {
        // TODO Auto-generated method stub

    }

    @Override
    public void giveExp(int amount) {
        getHandle().addExperience(amount);
    }

    @Override
    public void giveExpLevels(int amount) {
        // TODO Auto-generated method stub
    }

    @Override
    public float getExp() {
        return getHandle().experienceProgress;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        getHandle().experienceProgress = exp;
    }

    @Override
    public int getLevel() {
        return getHandle().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        getHandle().experienceLevel = level;
    }

    @Override
    public int getTotalExperience() {
        return getHandle().totalExperience;
    }

    @Override
    public void setTotalExperience(int exp) {
        getHandle().totalExperience = exp;
    }

    @Override
    public float getExhaustion() {
        // TODO Needs to mixin into player.HungerManager
        return 0;
    }

    @Override
    public void setExhaustion(float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public float getSaturation() {
        return getHandle().getHungerManager().getSaturationLevel();
    }

    @Override
    public void setSaturation(float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getFoodLevel() {
        return getHandle().getHungerManager().getFoodLevel();
    }

    @Override
    public void setFoodLevel(int value) {
        getHandle().getHungerManager().setFoodLevel(value);
    }

    @Override
    public boolean getAllowFlight() {
        return getHandle().abilities.allowFlying;
    }

    @Override
    public void setAllowFlight(boolean flight) {
        getHandle().abilities.allowFlying = flight;
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public void hidePlayer(@NotNull Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPlayer(@NotNull Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canSee(@NotNull Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFlying() {
        return ((PlayerAbilitiesAccessor) getHandle().abilities).allowsFlight();
    }

    @Override
    public void setFlying(boolean value) {
    	((PlayerAbilitiesAccessor) getHandle().abilities).setAllowsFlight(value);
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public float getFlySpeed() {
        return ((PlayerAbilitiesAccessor) getHandle().abilities).getFlySpeed();
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
    	((PlayerAbilitiesAccessor) getHandle().abilities).setFlySpeed(value);
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public float getWalkSpeed() {
        return ((PlayerAbilitiesAccessor) getHandle().abilities).getWalkSpeed();
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        ((PlayerAbilitiesAccessor) getHandle().abilities).setWalkSpeed(value);
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public void setTexturePack(@NotNull String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(@NotNull String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(@NotNull String url, @NotNull byte[] hash) {
        // TODO Auto-generated method stub

    }

    @Override
    public @NotNull Scoreboard getScoreboard() {
        return ((CraftScoreboardManager)this.server.getScoreboardManager()).getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        ServerPlayNetworkHandler playerConnection = this.getHandle().networkHandler;
        if (playerConnection == null) {
           throw new IllegalStateException("Cannot set scoreboard yet");
        } else {
        	((CraftScoreboardManager)this.server.getScoreboardManager()).setPlayerBoard(this, scoreboard);
        }

    }

    @Override
    public boolean isHealthScaled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getHealthScale() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public Entity getSpectatorTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        TitleS2CPacket times = new TitleS2CPacket(fadeIn, stay, fadeOut);
        getHandle().networkHandler.sendPacket(times);

        if (title != null) {
            TitleS2CPacket packetTitle = new TitleS2CPacket(TitleS2CPacket.Action.TITLE, ChatUtilities.fromStringOrNull(title));
            getHandle().networkHandler.sendPacket(packetTitle);
        }

        if (subtitle != null) {
            TitleS2CPacket packetSubtitle = new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, ChatUtilities.fromStringOrNull(subtitle));
            getHandle().networkHandler.sendPacket(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        TitleS2CPacket packetReset = new TitleS2CPacket(TitleS2CPacket.Action.RESET, null);
        getHandle().networkHandler.sendPacket(packetReset);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY,
                              double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                              double offsetY, double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY,
                                  double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                                  double offsetY, double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY,
                              double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                              double offsetY, double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY,
                                  double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                                  double offsetY, double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public @NotNull AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getClientViewDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public @NotNull String getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateCommands() {
        // TODO Auto-generated method stub

    }

    @Override
    public @NotNull org.bukkit.entity.Player.Spigot spigot() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + getUniqueId().hashCode();
        }
        return hash;
    }

	@Override
	public <T> T getMemory(MemoryKey<T> memoryKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setMemory(MemoryKey<T> memoryKey, T memoryValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Pose getPose() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersistentDataContainer getPersistentDataContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openBook(ItemStack book) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendExperienceChange(float progress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendExperienceChange(float progress, int level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(@NotNull Reason reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @Nullable Entity releaseLeftShoulderEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Entity releaseRightShoulderEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openSign(@NotNull Sign sign) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getLastLogin() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastSeen() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getProtocolVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public @Nullable InetSocketAddress getVirtualHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendActionBar(@NotNull String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendActionBar(char alternateChar, @NotNull String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlayerListHeaderFooter(@Nullable BaseComponent[] header, @Nullable BaseComponent[] footer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlayerListHeaderFooter(@Nullable BaseComponent header, @Nullable BaseComponent footer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSubtitle(BaseComponent[] subtitle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSubtitle(BaseComponent subtitle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showTitle(@Nullable BaseComponent[] title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showTitle(@Nullable BaseComponent title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showTitle(@Nullable BaseComponent[] title, @Nullable BaseComponent[] subtitle, int fadeInTicks,
			int stayTicks, int fadeOutTicks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showTitle(@Nullable BaseComponent title, @Nullable BaseComponent subtitle, int fadeInTicks,
			int stayTicks, int fadeOutTicks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTitle(@NotNull Title title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTitle(@NotNull Title title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideTitle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveExp(int amount, boolean applyMending) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int applyMending(int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getAffectsSpawning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAffectsSpawning(boolean affects) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getViewDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setViewDistance(int viewDistance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResourcePack(@NotNull String url, @NotNull String hash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @Nullable Status getResourcePackStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable String getResourcePackHash() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasResourcePack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public @NotNull PlayerProfile getPlayerProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlayerProfile(@NotNull PlayerProfile profile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getCooldownPeriod() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCooledAttackStrength(float adjustTicks) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resetCooldown() {
		// TODO Auto-generated method stub
		
	}
}
