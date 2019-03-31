package io.github.tr7zw.fabricbukkit.craftfabric;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOption;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

public class IntegratedServerImpl extends AbstractServerImpl {

    private IntegratedServer server;

    public IntegratedServerImpl(IntegratedServer server) {
        super(server);
        this.server = server;
    }

    @Override
    public int getViewDistance() {
        return (int) GameOption.RENDER_DISTANCE.get(MinecraftClient.getInstance().options);
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

}
