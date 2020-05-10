package io.github.craftfabric.craftfabric;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.destroystokyo.paper.profile.PlayerProfile;

public class DedicatedServerImpl extends AbstractServerImpl {

    private MinecraftDedicatedServer server;

    public DedicatedServerImpl(MinecraftDedicatedServer server) {
        super(server);
        this.server = server;
    }

    @Override
    public int getViewDistance() {
        return server.getProperties().viewDistance;
    }

    @Override
    @NotNull
    public String getWorldType() {
        return server.getProperties().levelType.getName().toUpperCase();
    }

    @Override
    public boolean getGenerateStructures() {
        return server.getProperties().generateStructures;
    }

    @Override
    public boolean getAllowNether() {
        return server.getProperties().allowNether;
    }

    @Override
    public boolean hasWhitelist() {
        return server.getProperties().whiteList.get();
    }

    @Override
    public void setWhitelist(boolean value) {
        server.setUseWhitelist(value);
    }

    @Override
    public boolean getOnlineMode() {
        return server.getProperties().onlineMode;
    }

    @Override
    public boolean getAllowFlight() {
        return server.getProperties().allowFlight;
    }

    @Override
    public boolean isHardcore() {
        return server.getProperties().hardcore;
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
