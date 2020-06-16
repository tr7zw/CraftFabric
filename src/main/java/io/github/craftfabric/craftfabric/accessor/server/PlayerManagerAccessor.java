package io.github.craftfabric.craftfabric.accessor.server;

import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerManager.class)
public interface PlayerManagerAccessor {
    @Invoker
    void invokeSendScoreboard(ServerScoreboard serverScoreboard, ServerPlayerEntity playerEntity);
}
