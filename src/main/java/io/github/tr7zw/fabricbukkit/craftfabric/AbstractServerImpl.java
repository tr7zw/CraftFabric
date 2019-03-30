package io.github.tr7zw.fabricbukkit.craftfabric;

import io.github.tr7zw.fabricbukkit.craftfabric.command.CommandMap;
import io.github.tr7zw.fabricbukkit.craftfabric.command.CraftConsoleCommandSender;
import io.github.tr7zw.fabricbukkit.craftfabric.world.WorldImpl;
import jline.console.ConsoleReader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.config.BannedIpEntry;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.LogManager;
import org.bukkit.*;
import org.bukkit.BanList.Type;
import org.bukkit.Warning.WarningState;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.bukkit.util.permissions.DefaultPermissions;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractServerImpl implements Server {
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();

    private final String serverName = "FabricBukkit";
    private final String serverVersion;
    private final String bukkitVersion = Versioning.getBukkitVersion();

    private final MinecraftServer server;
    private final Logger logger = Logger.getLogger("Minecraft");
    private final CommandMap commandMap = new CommandMap(this);
    private final SimplePluginManager pluginManager = new SimplePluginManager(this, commandMap);
    private ConsoleReader reader;
    private org.bukkit.command.ConsoleCommandSender console;

    public AbstractServerImpl(MinecraftServer server) {
        this.server = server;
        //serverVersion = ServerImpl.class.getPackage().getImplementationVersion(); TODO
        serverVersion = "1.14 Dev";

        try {
            reader = new ConsoleReader(System.in, System.out);
            reader.setExpandEvents(false); // Avoid parsing exceptions for uncommonly used event designators
        } catch (Throwable e) {
            try {
                // Try again with jline disabled for Windows users without C++ 2008 Redistributable
                System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
                System.setProperty("user.language", "en");
                //Main.useJline = false;
                reader = new ConsoleReader(System.in, System.out);
                reader.setExpandEvents(false);
            } catch (IOException ex) {
                LOG.warn((String) null, ex);
            }
        }
    }

    public void setupServer() {
        console = new CraftConsoleCommandSender();
        loadPlugins();
        enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
    }

    public void loadPlugins() {
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = new File("plugins");

        if (pluginFolder.exists()) {
            Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
            for (Plugin plugin : plugins) {
                try {
                    String message = String.format("Loading %s", plugin.getDescription().getFullName());
                    plugin.getLogger().info(message);
                    plugin.onLoad();
                } catch (Throwable ex) {
                    Logger.getLogger(AbstractServerImpl.class.getName()).log(Level.SEVERE, ex.getMessage() + " initializing "
                            + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
                }
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            // helpMap.clear();
            // helpMap.initializeGeneralTopics();
        }

        Plugin[] plugins = pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if ((!plugin.isEnabled())) { //  && (plugin.getDescription().getLoad() == type)
                enablePlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            // commandMap.setFallbackCommands();
            // setVanillaCommands();
            // commandMap.registerServerAliases();
            DefaultPermissions.registerCorePermissions();
            // CraftDefaultPermissions.registerCorePermissions();
            // loadCustomPermissions();
            // helpMap.initializeCommands();
            // syncCommands();
        }
    }

    private void enablePlugin(Plugin plugin) {
        try {
            List<Permission> perms = plugin.getDescription().getPermissions();

            for (Permission perm : perms) {
                try {
                    pluginManager.addPermission(perm, false);
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName()
                            + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                }
            }
            pluginManager.dirtyPermissibles();

            pluginManager.enablePlugin(plugin);
        } catch (Throwable ex) {
            Logger.getLogger(AbstractServerImpl.class.getName()).log(Level.SEVERE,
                    ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }

    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

    public ConsoleReader getReader() {
        return reader;
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
        // TODO Auto-generated method stub
    }

    @Override
    @NotNull
    public Set<String> getListeningPluginChannels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public String getName() {
        return serverName;
    }

    @Override
    @NotNull
    public String getVersion() {
        return serverVersion + " (MC: " + server.getVersion() + ")";
    }

    @Override
    @NotNull
    public String getBukkitVersion() {
        return bukkitVersion;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public Collection<? extends Player> getOnlinePlayers() {
        return server.getPlayerManager().getPlayerList().stream().map(player -> ((CraftLink<Player>) (Object) player).getCraftHandler()).collect(Collectors.toList());
    }

    @Override
    public int getMaxPlayers() {
        return server.getMaxPlayerCount();
    }

    @Override
    public int getPort() {
        return server.getServerPort();
    }

    @Override
    public abstract int getViewDistance();

    @Override
    @NotNull
    public String getIp() {
        return server.getServerIp();
    }

    @Override
    @NotNull
    public String getServerName() {
        return server.getServerName(); // TODO: hmm, is this the right name? It isn't a standard server.properties entry!
    }

    @Override
    @NotNull
    public String getServerId() {
        return ""; // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config
    }

    @Override
    @NotNull
    public abstract String getWorldType();

    @Override
    public abstract boolean getGenerateStructures();

    @Override
    public boolean getAllowEnd() {
        return true; // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config
    }

    @Override
    public abstract boolean getAllowNether();

    @Override
    public abstract boolean hasWhitelist();

    @Override
    public abstract void setWhitelist(boolean value);

    @Override
    @NotNull
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reloadWhitelist() {
        server.getPlayerManager().reloadWhitelist();
    }

    @Override
    public int broadcastMessage(@NotNull String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    @Override
    @NotNull
    public String getUpdateFolder() {
        return "update"; // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config
    }

    @Override
    @NotNull
    public File getUpdateFolderFile() {
        return new File("./plugins/" + getUpdateFolder()); // FIXME: read plugin folder from cli argument
    }

    @Override
    public long getConnectionThrottle() {
        return -1; // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config, remember that is bungee mode enabled this MUST be -1!
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return 400; // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return 1; // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config
    }

    @Override
    public Player getPlayer(@NotNull String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Player getPlayerExact(@NotNull String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public List<Player> matchPlayer(@NotNull String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Player getPlayer(@NotNull UUID id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    @NotNull
    public BukkitScheduler getScheduler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public ServicesManager getServicesManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public List<World> getWorlds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public World createWorld(@NotNull WorldCreator creator) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean unloadWorld(@NotNull String name, boolean save) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadWorld(@NotNull World world, boolean save) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public World getWorld(@NotNull String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public World getWorld(@NotNull UUID uid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MapView getMap(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public MapView createMap(@NotNull World world) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType, int radius,
                                       boolean findUnexplored) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reload() {
        // TODO Auto-generated method stub
    }

    @Override
    public void reloadData() {
        // TODO Auto-generated method stub
    }

    @Override
    @NotNull
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginCommand getPluginCommand(@NotNull String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void savePlayers() {
        // TODO plugin-induced save warning, look at bukkit implementation
        server.getPlayerManager().saveAllPlayerData();
    }

    @Override
    public boolean dispatchCommand(@NotNull CommandSender sender, @NotNull String commandLine) throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @NotNull
    public List<Recipe> getRecipesFor(@NotNull ItemStack result) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Iterator<Recipe> recipeIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearRecipes() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resetRecipes() {
        // TODO Auto-generated method stub
    }

    @Override
    @NotNull
    public Map<String, String[]> getCommandAliases() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSpawnRadius() {
        return 16; // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config
    }

    @Override
    public void setSpawnRadius(int value) {
        // FIXME: this is a new property introduced by Bukkit, we need to put this into a custom config
    }

    @Override
    public abstract boolean getOnlineMode();

    @Override
    public abstract boolean getAllowFlight();

    @Override
    public abstract boolean isHardcore();

    @Override
    public abstract void shutdown();

    @Override
    public int broadcast(@NotNull String message, @NotNull String permission) {
        Set<CommandSender> recipients = new HashSet<>();
        for (Permissible permissible : getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                recipients.add((CommandSender) permissible);
            }
        }

        BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(message, recipients);
        getPluginManager().callEvent(broadcastMessageEvent);

        if (broadcastMessageEvent.isCancelled()) {
            return 0;
        }

        message = broadcastMessageEvent.getMessage();

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    @Override
    @NotNull
    public OfflinePlayer getOfflinePlayer(@NotNull String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public OfflinePlayer getOfflinePlayer(@NotNull UUID id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Set<String> getIPBans() {
        // FIXME: is getNames() correct?
        return new HashSet<>(Arrays.asList(server.getPlayerManager().getIpBanList().getNames()));
    }

    @Override
    public void banIP(@NotNull String address) {
        Validate.notNull(address, "Address cannot be null.");
        server.getPlayerManager().getIpBanList().add(new BannedIpEntry(address));
    }

    @Override
    public void unbanIP(@NotNull String address) {
        Validate.notNull(address, "Address cannot be null.");
        server.getPlayerManager().getIpBanList().remove(address);
    }

    @Override
    @NotNull
    public Set<OfflinePlayer> getBannedPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public BanList getBanList(@NotNull Type type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Set<OfflinePlayer> getOperators() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(server.getWorld(DimensionType.OVERWORLD).getLevelProperties().getGameMode().getId());
    }

    @Override
    public void setDefaultGameMode(@NotNull GameMode mode) {
        Validate.notNull(mode, "Mode cannot be null");

        for (World world : getWorlds()) {
            ((WorldImpl) world).getHandle().getLevelProperties().setGameMode(net.minecraft.world.GameMode.byId(mode.getValue()));
        }
    }

    @Override
    @NotNull
    public ConsoleCommandSender getConsoleSender() {
        return console;
    }

    @Override
    @NotNull
    public File getWorldContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public OfflinePlayer[] getOfflinePlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Messenger getMessenger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public HelpMap getHelpMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Inventory createInventory(InventoryHolder owner, @NotNull InventoryType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Inventory createInventory(InventoryHolder owner, @NotNull InventoryType type, @NotNull String title) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Inventory createInventory(InventoryHolder owner, int size, @NotNull String title) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Merchant createMerchant(String title) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMonsterSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isPrimaryThread() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @NotNull
    public String getMotd() {
        return server.getServerMotd();
    }

    @Override
    public String getShutdownMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public WarningState getWarningState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public ItemFactory getItemFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CachedServerIcon getServerIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public CachedServerIcon loadServerIcon(@NotNull File file) throws IllegalArgumentException, Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public CachedServerIcon loadServerIcon(@NotNull BufferedImage image) throws IllegalArgumentException, Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getIdleTimeout() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setIdleTimeout(int threshold) {
        // TODO Auto-generated method stub
    }

    @Override
    @NotNull
    public ChunkData createChunkData(@NotNull World world) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public BossBar createBossBar(String title, @NotNull BarColor color, @NotNull BarStyle style, @NotNull BarFlag... flags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public KeyedBossBar createBossBar(@NotNull NamespacedKey key, String title, @NotNull BarColor color, @NotNull BarStyle style,
                                      @NotNull BarFlag... flags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Iterator<KeyedBossBar> getBossBars() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KeyedBossBar getBossBar(@NotNull NamespacedKey key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeBossBar(@NotNull NamespacedKey key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Entity getEntity(@NotNull UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Advancement getAdvancement(@NotNull NamespacedKey key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public Iterator<Advancement> advancementIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public BlockData createBlockData(@NotNull Material material) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public BlockData createBlockData(@NotNull Material material, Consumer<BlockData> consumer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public BlockData createBlockData(@NotNull String data) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public BlockData createBlockData(Material material, String data) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Keyed> Tag<T> getTag(@NotNull String registry, @NotNull NamespacedKey tag, @NotNull Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String registry, @NotNull Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LootTable getLootTable(@NotNull NamespacedKey key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public List<Entity> selectEntities(@NotNull CommandSender sender, @NotNull String selector) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @NotNull
    public UnsafeValues getUnsafe() {
        return CraftMagicNumbers.INSTANCE;
    }

    @Override
    @NotNull
    public Spigot spigot() {
        // TODO Auto-generated method stub
        return null;
    }
}
