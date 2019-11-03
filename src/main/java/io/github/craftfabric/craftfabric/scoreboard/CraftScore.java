package io.github.craftfabric.craftfabric.scoreboard;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardPlayerScore;

final class CraftScore implements Score {
   private final String entry;
   private final CraftObjective objective;

   CraftScore(CraftObjective objective, String entry) {
      this.objective = objective;
      this.entry = entry;
   }

   public OfflinePlayer getPlayer() {
      return Bukkit.getOfflinePlayer(this.entry);
   }

   public String getEntry() {
      return this.entry;
   }

   public Objective getObjective() {
      return this.objective;
   }

   public int getScore() throws IllegalStateException {
      Scoreboard board = this.objective.checkState().board;
      if (board.getKnownPlayers().contains(this.entry)) {
         Map scores = board.getPlayerObjectives(this.entry);
         ScoreboardPlayerScore score = (ScoreboardPlayerScore)scores.get(this.objective.getHandle());
         if (score != null) {
            return score.getScore();
         }
      }

      return 0;
   }

   public void setScore(int score) throws IllegalStateException {
      this.objective.checkState().board.getPlayerScore(this.entry, this.objective.getHandle()).setScore(score);
   }

   public boolean isScoreSet() throws IllegalStateException {
      Scoreboard board = this.objective.checkState().board;
      return board.getKnownPlayers().contains(this.entry) && board.getPlayerObjectives(this.entry).containsKey(this.objective.getHandle());
   }

   public CraftScoreboard getScoreboard() {
      return this.objective.getScoreboard();
   }
}

