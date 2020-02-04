package io.github.craftfabric.craftfabric.world;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.block.CraftBlock;
import io.github.craftfabric.craftfabric.mixin.IWorldMixin;
import io.github.craftfabric.craftfabric.utility.EnvironmentTypeUtilities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;

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

import com.destroystokyo.paper.HeightmapType;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class CraftWorld implements World {

    private final net.minecraft.world.World world;
    private final ChunkGenerator generator;
    private Environment environment;

    public CraftWorld(net.minecraft.world.World world, ChunkGenerator generator, Environment environment) {
        this.world = world;
        this.generator = generator;
        this.environment = environment;
    }

    public net.minecraft.world.World getHandle() {
        return world;
    }

    private AbstractServerImpl getServer() {
        return (AbstractServerImpl) Bukkit.getServer();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        // TODO Auto-generated method stub

    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
    	return new CraftBlock(world, new BlockPos(x, y, z));
    }

    @Override
    public Block getBlockAt(Location location) {
    	return new CraftBlock(world, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        if (!this.isChunkLoaded(x >> 4, z >> 4)) {
            this.getChunkAt(x >> 4, z >> 4);
         }

         return this.world.getTopPosition(Type.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk getChunkAt(Location location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk getChunkAt(Block block) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Chunk[] getLoadedChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void loadChunk(Chunk chunk) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void loadChunk(int x, int z) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item dropItem(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean generateTree(Location location, TreeType type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Entity> getEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Player> getPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z,
                                                Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance,
                                           Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize,
                                           Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance,
                                         FluidCollisionMode fluidCollisionMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance,
                                         FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance,
                                   FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize,
                                   Predicate<Entity> filter) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public String getName() {
		// return world.getLevelProperties().getLevelName(); //TODO this would be right
		// if there was multiworld support
		switch (getEnvironment()) { // FIXME fix till then
		case NORMAL:
			return "world";
		case NETHER:
			return "nether";
		case THE_END:
			return "end";
		default:
			return "unknown";
		}
	}

    @Override
    public UUID getUID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this, world.getLevelProperties().getSpawnX(), world.getLevelProperties().getSpawnY(), world.getLevelProperties().getSpawnZ());
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        return setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        this.world.setSpawnPos(new BlockPos(x, y, z));
        return true;
    }

    @Override
    public long getTime() {
        return this.world.getTimeOfDay();
    }

    @Override
    public void setTime(long time) {
        this.world.setTimeOfDay(time);
    }

    @Override
    public long getFullTime() {
        return this.world.getTime();
    }

    @Override
    public void setFullTime(long time) {
    	this.world.setTime(time);
    }

    @Override
    public boolean hasStorm() {
        return this.world.getLevelProperties().isRaining();
    }

    @Override
    public void setStorm(boolean hasStorm) { //TODO Broken?
        this.world.getLevelProperties().setRaining(hasStorm);
        ((IWorldMixin)this.world).updateWeather();
    }

    @Override
    public int getWeatherDuration() {
        return this.world.getLevelProperties().getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) { //TODO Broken?
        this.world.getLevelProperties().setRainTime(duration);
        ((IWorldMixin)this.world).updateWeather();
    }

    @Override
    public boolean isThundering() {
    	return this.world.getLevelProperties().isThundering();
    }

    @Override
    public void setThundering(boolean thundering) { //TODO Broken?
        this.world.getLevelProperties().setThundering(thundering);
        ((IWorldMixin)this.world).updateWeather();
    }

    @Override
    public int getThunderDuration() {
    	return this.world.getLevelProperties().getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) { //TODO Broken?
    	this.world.getLevelProperties().setThunderTime(duration);
    	((IWorldMixin)this.world).updateWeather();
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Environment getEnvironment() {
        return EnvironmentTypeUtilities.fromNMS(world.getDimension().getType());
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public boolean getPVP() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPVP(boolean pvp) {
        // TODO Auto-generated method stub

    }

    @Override
    public ChunkGenerator getGenerator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<BlockPopulator> getPopulators() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data, int radius) {
        // TODO Auto-generated method stub

    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getAllowAnimals() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getAllowMonsters() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Biome getBiome(int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getTemperature(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getHumidity(int x, int z) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return world.getHeight();
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isAutoSave() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAutoSave(boolean value) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("deprecation")
	@Override
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
    	this.getHandle().getLevelProperties().setDifficulty(net.minecraft.world.Difficulty.byOrdinal(difficulty.getValue()));
    }

    @Override
    public File getWorldFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WorldType getWorldType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canGenerateStructures() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getTicksPerMonsterSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getMonsterSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getAmbientSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
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
    public String[] getGameRules() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getGameRuleValue(String rule) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGameRule(String rule) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public WorldBorder getWorldBorder() {
        // TODO Auto-generated method stub
        return null;
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
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
                                  double offsetZ, double extra, T data, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
                                  double offsetY, double offsetZ, double extra, T data, boolean force) {
        // TODO Auto-generated method stub

    }

    @Override
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius,
                                           boolean findUnexplored) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Spigot spigot() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removePluginChunkTickets(Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Plugin> getPluginChunkTickets(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends AbstractArrow> T spawnArrow(Location location, Vector direction, float speed, float spread,
			Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks,
			Entity source) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getViewDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Raid locateNearestRaid(Location location, int radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Raid> getRaids() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || !(obj instanceof CraftWorld) || ((CraftWorld)obj).getName().equals(getName()); //TODO meh
	}

	@Override
	public @NotNull Biome getBiome(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBiome(int x, int y, int z, @NotNull Biome bio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getTemperature(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHumidity(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isHardcore() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHardcore(boolean hardcore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getEntityCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTileEntityCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTickableTileEntityCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChunkCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHighestBlockYAt(int x, int z, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Entity getEntity(@NotNull UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDayTime() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire,
			boolean breakBlocks) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @Nullable Player source,
			double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra,
			@Nullable T data, boolean force) {
		// TODO Auto-generated method stub
		
	}


}
