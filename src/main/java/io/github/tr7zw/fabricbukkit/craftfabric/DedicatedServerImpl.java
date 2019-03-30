package io.github.tr7zw.fabricbukkit.craftfabric;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class DedicatedServerImpl extends ServerImpl{

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

}
