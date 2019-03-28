package io.github.tr7zw.fabricbukkit;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class WorldImpl implements World {

    public net.minecraft.world.World getHandle() {
        return null;
    }

    @Override
    public @NotNull Block getBlockAt(int x, int y, int z) {
        return null;
    }

    @Override
    public @NotNull Block getBlockAt(@NotNull Location location) {
        return null;
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return 0;
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location) {
        return 0;
    }

    @Override
    public @NotNull Block getHighestBlockAt(int x, int z) {
        return null;
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location) {
        return null;
    }

    @Override
    public @NotNull Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Location location) {
        return null;
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Block block) {
        return null;
    }

    @Override
    public boolean isChunkLoaded(@NotNull Chunk chunk) {
        return false;
    }

    @NotNull
    @Override
    public @NotNull Chunk[] getLoadedChunks() {
        return new Chunk[0];
    }

    @Override
    public void loadChunk(@NotNull Chunk chunk) {

    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return false;
    }

    @Override
    public void loadChunk(int x, int z) {

    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        return false;
    }

    @Override
    public boolean unloadChunk(@NotNull Chunk chunk) {
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        return false;
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        return false;
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return false;
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {

    }

    @Override
    public @NotNull Collection<Chunk> getForceLoadedChunks() {
        return null;
    }

    @Override
    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
        return null;
    }

    @Override
    public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {
        return null;
    }

    @Override
    public @NotNull Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread) {
        return null;
    }

    @Override
    public <T extends Arrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread, @NotNull Class<T> clazz) {
        return null;
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull TreeType type) {
        return false;
    }

    @Override
    public boolean generateTree(@NotNull Location loc, @NotNull TreeType type, @NotNull BlockChangeDelegate delegate) {
        return false;
    }

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type) {
        return null;
    }

    @Override
    public @NotNull LightningStrike strikeLightning(@NotNull Location loc) {
        return null;
    }

    @Override
    public @NotNull LightningStrike strikeLightningEffect(@NotNull Location loc) {
        return null;
    }

    @Override
    public @NotNull List<Entity> getEntities() {
        return null;
    }

    @Override
    public @NotNull List<LivingEntity> getLivingEntities() {
        return null;
    }

    @Override
    public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull @NotNull Class<T>... classes) {
        return null;
    }

    @Override
    public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {
        return null;
    }

    @Override
    public @NotNull Collection<Entity> getEntitiesByClasses(@NotNull @NotNull Class<?>... classes) {
        return null;
    }

    @Override
    public @NotNull List<Player> getPlayers() {
        return null;
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
        return null;
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<Entity> filter) {
        return null;
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
        return null;
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<Entity> filter) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, @Nullable Predicate<Entity> filter) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize, @Nullable Predicate<Entity> filter) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        return null;
    }

    @Override
    public @Nullable RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, @Nullable Predicate<Entity> filter) {
        return null;
    }

    @Override
    public @NotNull String getName() {
        return null;
    }

    @Override
    public @NotNull UUID getUID() {
        return null;
    }

    @Override
    public @NotNull Location getSpawnLocation() {
        return null;
    }

    @Override
    public @NotNull boolean setSpawnLocation(@NotNull Location location) {
        return false;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return false;
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public void setTime(long time) {

    }

    @Override
    public long getFullTime() {
        return 0;
    }

    @Override
    public void setFullTime(long time) {

    }

    @Override
    public boolean hasStorm() {
        return false;
    }

    @Override
    public void setStorm(boolean hasStorm) {

    }

    @Override
    public int getWeatherDuration() {
        return 0;
    }

    @Override
    public void setWeatherDuration(int duration) {

    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public void setThundering(boolean thundering) {

    }

    @Override
    public int getThunderDuration() {
        return 0;
    }

    @Override
    public void setThunderDuration(int duration) {

    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power) {
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {
        return false;
    }

    @Override
    public @NotNull Environment getEnvironment() {
        return null;
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public boolean getPVP() {
        return false;
    }

    @Override
    public void setPVP(boolean pvp) {

    }

    @Override
    public @Nullable ChunkGenerator getGenerator() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public @NotNull List<BlockPopulator> getPopulators() {
        return null;
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {
        return null;
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function) throws IllegalArgumentException {
        return null;
    }

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {

    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius) {

    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data) {

    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data, int radius) {

    }

    @Override
    public @NotNull ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        return null;
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {

    }

    @Override
    public boolean getAllowAnimals() {
        return false;
    }

    @Override
    public boolean getAllowMonsters() {
        return false;
    }

    @Override
    public @NotNull Biome getBiome(int x, int z) {
        return null;
    }

    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {

    }

    @Override
    public double getTemperature(int x, int z) {
        return 0;
    }

    @Override
    public double getHumidity(int x, int z) {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 0;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return false;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {

    }

    @Override
    public boolean isAutoSave() {
        return false;
    }

    @Override
    public void setAutoSave(boolean value) {

    }

    @Override
    public void setDifficulty(@NotNull Difficulty difficulty) {

    }

    @Override
    public @NotNull Difficulty getDifficulty() {
        return null;
    }

    @Override
    public @NotNull File getWorldFolder() {
        return null;
    }

    @Override
    public @Nullable WorldType getWorldType() {
        return null;
    }

    @Override
    public boolean canGenerateStructures() {
        return false;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {

    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {

    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {

    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {

    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {

    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {

    }

    @NotNull
    @Override
    public @NotNull String[] getGameRules() {
        return new String[0];
    }

    @Override
    public @Nullable String getGameRuleValue(@Nullable String rule) {
        return null;
    }

    @Override
    public boolean setGameRuleValue(@NotNull String rule, @NotNull String value) {
        return false;
    }

    @Override
    public boolean isGameRule(@NotNull String rule) {
        return false;
    }

    @Override
    public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> rule) {
        return null;
    }

    @Override
    public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> rule) {
        return null;
    }

    @Override
    public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue) {
        return false;
    }

    @Override
    public @NotNull WorldBorder getWorldBorder() {
        return null;
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {

    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {

    }

    @Override
    public @Nullable Location locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
        return null;
    }

    @Override
    public @NotNull Spigot spigot() {
        return null;
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {

    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {

    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {

    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        return null;
    }
}
