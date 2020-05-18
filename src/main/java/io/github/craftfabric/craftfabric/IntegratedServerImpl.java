package io.github.craftfabric.craftfabric;

import java.io.File;
import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.destroystokyo.paper.profile.PlayerProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.Option;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
public class IntegratedServerImpl extends AbstractServerImpl {

    private IntegratedServer server;

    public IntegratedServerImpl(IntegratedServer server) {
        super(server);
        this.server = server;
    }

    @Override
    public int getViewDistance() {
        return (int) Option.RENDER_DISTANCE.get(MinecraftClient.getInstance().options);
    }

    @Override
    @NotNull
    public String getWorldType() {
        return server.getWorld(DimensionType.OVERWORLD).getGeneratorType().getName();
    }

    @Override
    public boolean getGenerateStructures() {
        return server.getWorld(DimensionType.OVERWORLD).getLevelProperties().hasStructures();
    }

    @Override
    @NotNull
    public File getUpdateFolderFile() {
        return new File(getWorlds().get(0).getWorldFolder(), "plugins/" + getUpdateFolder());
    }
    
    @Override
    public File pluginDirectory() {
    	return new File(server.getLevelStorage().getSavesDirectory().resolve(server.getLevelName()).toFile(), "plugins");
    }
    
    @Override
    public boolean getAllowNether() {
        return true; // Not a Vanilla singleplayer option
    }

    @Override
    public boolean hasWhitelist() {
        return false; // Only singleplayer and LAN
    }

    @Override
    public void setWhitelist(boolean value) {
        // Ignore
    }

    @Override
    public boolean getOnlineMode() {
        return false; // Not needed for singleplayer and LAN
    }

    @Override
    public boolean getAllowFlight() {
        return false; // The vanilla flightchecks are enabled
    }

    @Override
    public boolean isHardcore() {
        return server.getWorld(DimensionType.OVERWORLD).getLevelProperties().isHardcore();
    }

    @Override
    public void shutdown() {
        // FIXME: just a placeholder!
        server.shutdown();
    }

	@Override
	public @Nullable UUID getPlayerUniqueId(@NotNull String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull double[] getTPS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reloadPermissions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean reloadCommandAliases() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean suggestPlayerNamesWhenNullTabCompletions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public @NotNull String getPermissionMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull PlayerProfile createProfile(@NotNull UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull PlayerProfile createProfile(@NotNull String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
