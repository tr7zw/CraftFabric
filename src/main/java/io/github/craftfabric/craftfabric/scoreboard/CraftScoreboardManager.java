package io.github.craftfabric.craftfabric.scoreboard;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import io.github.craftfabric.craftfabric.accessor.server.PlayerManagerAccessor;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

import io.github.craftfabric.craftfabric.entity.CraftPlayer;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class CraftScoreboardManager implements ScoreboardManager {
   private final CraftScoreboard mainScoreboard;
   private final MinecraftServer server;
   private final Collection scoreboards = Collections.newSetFromMap(new WeakHashMap<Object, Boolean>());
   private final Map playerBoards = new HashMap();

   public CraftScoreboardManager(MinecraftServer minecraftserver, Scoreboard scoreboardServer) {
      this.mainScoreboard = new CraftScoreboard(scoreboardServer);
      this.server = minecraftserver;
      this.scoreboards.add(this.mainScoreboard);
   }

   public CraftScoreboard getMainScoreboard() {
      return this.mainScoreboard;
   }

   public CraftScoreboard getNewScoreboard() {
      //AsyncCatcher.catchOp("scoreboard creation");
      CraftScoreboard scoreboard = new CraftScoreboard(new ServerScoreboard(this.server));
      this.scoreboards.add(scoreboard);
      return scoreboard;
   }

   public CraftScoreboard getPlayerBoard(CraftPlayer player) {
      CraftScoreboard board = (CraftScoreboard)this.playerBoards.get(player);
      return board == null ? this.getMainScoreboard() : board;
   }

   public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) throws IllegalArgumentException {
      Validate.isTrue(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");
      CraftScoreboard scoreboard = (CraftScoreboard)bukkitScoreboard;
      Scoreboard oldboard = this.getPlayerBoard(player).getHandle();
      Scoreboard newboard = scoreboard.getHandle();
      ServerPlayerEntity entityplayer = player.getHandle();
      if (oldboard != newboard) {
         if (scoreboard == this.mainScoreboard) {
            this.playerBoards.remove(player);
         } else {
            this.playerBoards.put(player, scoreboard);
         }

         HashSet removed = new HashSet();

         for(int i = 0; i < 3; ++i) {
            ScoreboardObjective scoreboardobjective = oldboard.getObjectiveForSlot(i);
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
               entityplayer.networkHandler.sendPacket(new ScoreboardObjectiveUpdateS2CPacket(scoreboardobjective, 1));
               removed.add(scoreboardobjective);
            }
         }

         Iterator iterator = oldboard.getTeams().iterator();

         while(iterator.hasNext()) {
            Team scoreboardteam = (Team)iterator.next();
            entityplayer.networkHandler.sendPacket(new TeamS2CPacket(scoreboardteam, 1));
         }

         ((PlayerManagerAccessor) this.server.getPlayerManager()).invokeSendScoreboard((ServerScoreboard) newboard, player.getHandle());
      }
   }

   public void removePlayer(Player player) {
      this.playerBoards.remove(player);
   }

   public void getScoreboardScores(ScoreboardCriterion criteria, String name, Consumer consumer) {
      Iterator var5 = this.scoreboards.iterator();

      while(var5.hasNext()) {
         CraftScoreboard scoreboard = (CraftScoreboard)var5.next();
         Scoreboard board = scoreboard.board;
         board.forEachScore(criteria, name, (score) -> {
            consumer.accept(score);
         });
      }

   }
}
