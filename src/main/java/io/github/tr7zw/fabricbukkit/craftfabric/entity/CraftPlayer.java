package io.github.tr7zw.fabricbukkit.craftfabric.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.github.tr7zw.fabricbukkit.craftfabric.util.ChatUtils;
import io.github.tr7zw.fabricbukkit.craftfabric.util.NamespaceUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.scoreboard.ScoreboardTeam;
import net.minecraft.server.config.WhitelistEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.ChatMessageC2SPacket;
import net.minecraft.sortme.ChatMessageType;
import net.minecraft.stat.Stats;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;

import java.net.InetSocketAddress;
import java.util.*;

@SuppressWarnings("deprecation")
public class CraftPlayer extends CraftHumanEntity implements Player {

    private final ServerPlayerEntity handler;
    private final Set<String> channels = new HashSet<>();

    public CraftPlayer(ServerPlayerEntity handler) {
        super(handler);
        this.handler = handler;
    }

    public ServerPlayerEntity getHandler() {
        return handler;
    }

    public GameProfile getProfile() {
        return getHandler().getGameProfile();
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

    @Override
    public void closeInventory() {
        handler.closeGui();
    }

    @Override
    public GameMode getGameMode() {
        GameMode mode = GameMode.valueOf(getHandler().interactionManager.getGameMode().name());
        return mode != null ? mode : GameMode.SURVIVAL;
    }

    @Override
    public void setGameMode(GameMode mode) {
        getHandler().interactionManager.setGameMode(net.minecraft.world.GameMode.byName(mode.name(), net.minecraft.world.GameMode.SURVIVAL));
    }

    private Collection<Recipe<?>> bukkitKeysToMinecraftRecipes(Collection<NamespacedKey> recipeKeys) {
        Collection<Recipe<?>> recipes = new ArrayList<>();
        RecipeManager manager = getHandler().world.getServer().getRecipeManager();

        for (NamespacedKey recipeKey : recipeKeys) {
            Optional<? extends Recipe<?>> recipe = manager.get(NamespaceUtils.toMinecraft(recipeKey));
            if (!recipe.isPresent()) {
                continue;
            }

            recipes.add(recipe.get());
        }

        return recipes;
    }

    @Override
    public void sendMessage(String message) {
        getHandler().sendChatMessage(new StringTextComponent(message), ChatMessageType.SYSTEM);
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        return getHandler().getRecipeBook().unlockRecipes(bukkitKeysToMinecraftRecipes(recipes), getHandler());
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        return 1 == getHandler().getRecipeBook().lockRecipes(bukkitKeysToMinecraftRecipes(Collections.singleton(recipe)), getHandler());
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        return getHandler().getRecipeBook().lockRecipes(bukkitKeysToMinecraftRecipes(recipes), getHandler());
    }

    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isConversing() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void acceptConversationInput(String input) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        // TODO Auto-generated method stub

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

    @Override
    public long getFirstPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLastPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", getName());
        return result;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        if (getHandler().networkHandler == null) {
            return;
        }
        if (channels.contains(channel)) {
            channel = StandardMessenger.validateAndCorrectChannel(channel);
            CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(new Identifier(channel), new PacketByteBuf(Unpooled.wrappedBuffer(message)));
            getHandler().networkHandler.sendPacket(packet);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    @Override
    public String getDisplayName() {
        return getHandler().getDisplayName().getFormattedText();
    }

    @Override
    public void setDisplayName(String name) {
        // TODO Kinda hard to vanilla implement?
    }

    @Override
    public String getPlayerListName() {
        return ScoreboardTeam.modifyText(getHandler().getScoreboardTeam(), getHandler().getName()).getFormattedText();
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
    public Location getCompassTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCompassTarget(Location loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) getHandler().networkHandler.getConnection().getAddress();
    }

    @Override
    public void sendRawMessage(String message) {
        if (getHandler().networkHandler == null){
            return;
        }
        for (TextComponent component : ChatUtils.fromString(message)) {
            getHandler().networkHandler.sendPacket(new ChatMessageS2CPacket(component));
        }
    }

    @Override
    public void kickPlayer(String message) {
        getHandler().networkHandler.disconnect(new StringTextComponent(message));
    }

    @Override
    public void chat(String msg) {
        getHandler().networkHandler.onChatMessage(new ChatMessageC2SPacket(msg));
    }

    @Override
    public boolean performCommand(String command) {
        return Bukkit.getServer().dispatchCommand(this, command);
    }

    @Override
    public boolean isSneaking() {
        return getHandler().isSneaking();
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandler().setSneaking(true);
    }

    @Override
    public boolean isSprinting() {
        return getHandler().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getHandler().setSprinting(sprinting);
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
    public void playNote(Location loc, byte instrument, byte note) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(Sound sound) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(String sound) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(Sound sound, SoundCategory category) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSound(String sound, SoundCategory category) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBlockChange(Location loc, BlockData block) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMap(MapView map) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void awardAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Please use the Advancement methods!");
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Please use the Advancement methods!");
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Please use the Advancement methods!");
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getHandler().getStatHandler().increaseStat(getHandler(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        getHandler().getStatHandler().increaseStat(getHandler(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), -1);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        getHandler().getStatHandler().increaseStat(getHandler(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        getHandler().getStatHandler().increaseStat(getHandler(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), -1 * amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        getHandler().getStatHandler().setStat(getHandler(), Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())), newValue);
    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return getHandler().getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(new Identifier(statistic.toString().toLowerCase())));
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
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
    public void setPlayerWeather(WeatherType type) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetPlayerWeather() {
        // TODO Auto-generated method stub

    }

    @Override
    public void giveExp(int amount) {
        getHandler().addExperience(amount);
    }

    @Override
    public void giveExpLevels(int amount) {
        // TODO Auto-generated method stub
    }

    @Override
    public float getExp() {
        return getHandler().experienceBarProgress;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        getHandler().experienceBarProgress = exp;
    }

    @Override
    public int getLevel() {
        return getHandler().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        getHandler().experienceLevel = level;
    }

    @Override
    public int getTotalExperience() {
        return getHandler().experience;
    }

    @Override
    public void setTotalExperience(int exp) {
        getHandler().experience = exp;
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
        return getHandler().getHungerManager().getSaturationLevel();
    }

    @Override
    public void setSaturation(float value) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getFoodLevel() {
        return getHandler().getHungerManager().getFoodLevel();
    }

    @Override
    public void setFoodLevel(int value) {
        getHandler().getHungerManager().setFoodLevel(value);
    }

    @Override
    public boolean getAllowFlight() {
        return getHandler().abilities.allowFlying;
    }

    @Override
    public void setAllowFlight(boolean flight) {
        getHandler().abilities.allowFlying = flight;
    }

    @Override
    public void hidePlayer(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPlayer(Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canSee(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFlying() {
        return getHandler().abilities.flying;
    }

    @Override
    public void setFlying(boolean value) {
        getHandler().abilities.flying = value;
    }

    @Override
    public float getFlySpeed() {
        return getHandler().abilities.getFlySpeed();
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        getHandler().abilities.setFlySpeed(value);
    }

    @Override
    public float getWalkSpeed() {
        return getHandler().abilities.getWalkSpeed();
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        getHandler().abilities.setWalkSpeed(value);
    }

    @Override
    public void setTexturePack(String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        // TODO Auto-generated method stub

    }

    @Override
    public Scoreboard getScoreboard() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetTitle() {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
                              double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
                              double offsetY, double offsetZ) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
                                  double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
                                  double offsetY, double offsetZ, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
                              double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
                              double offsetY, double offsetZ, double extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
                                  double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
                                  double offsetY, double offsetZ, double extra, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getClientViewDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateCommands() {
        // TODO Auto-generated method stub

    }

    @Override
    public org.bukkit.entity.Player.Spigot spigot() {
        // TODO Auto-generated method stub
        return null;
    }


}
