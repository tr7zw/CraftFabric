package io.github.craftfabric.craftfabric.mixin;

import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.network.ServerPlayerEntity;

public interface IPlayerManager {

	public void sendScoreboardWrapper(ServerScoreboard serverScoreboard_1, ServerPlayerEntity serverPlayerEntity_1);
	
}
