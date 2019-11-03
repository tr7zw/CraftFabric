package io.github.craftfabric.craftfabric.scoreboard;

import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;

import io.github.craftfabric.craftfabric.utility.ChatUtilities;
import io.github.craftfabric.craftfabric.utility.ScoreboardUtils;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

final class CraftObjective extends CraftScoreboardComponent implements Objective {
   private final ScoreboardObjective objective;
   private final CraftCriteria criteria;

   CraftObjective(CraftScoreboard scoreboard, ScoreboardObjective objective) {
      super(scoreboard);
      this.objective = objective;
      this.criteria = CraftCriteria.getFromNMS(objective);
   }

   ScoreboardObjective getHandle() {
      return this.objective;
   }

   public String getName() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return this.objective.getName();
   }

   public String getDisplayName() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return ChatUtilities.fromComponent(this.objective.getDisplayName());
   }

   public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
      Validate.notNull(displayName, "Display name cannot be null");
      Validate.isTrue(displayName.length() <= 128, "Display name '" + displayName + "' is longer than the limit of 128 characters");
      CraftScoreboard scoreboard = this.checkState();
      this.objective.setDisplayName(ChatUtilities.fromString(displayName)[0]);
   }

   public String getCriteria() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return this.criteria.bukkitName;
   }

   public boolean isModifiable() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return !this.criteria.criteria.isReadOnly();
   }

   public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      Scoreboard board = scoreboard.board;
      ScoreboardObjective objective = this.objective;

      int slotNumber;
      for(slotNumber = 0; slotNumber < 3; ++slotNumber) {
         if (board.getObjectiveForSlot(slotNumber) == objective) {
            board.setObjectiveSlot(slotNumber, (ScoreboardObjective)null);
         }
      }

      if (slot != null) {
         slotNumber = ScoreboardUtils.fromBukkitSlot(slot);
         board.setObjectiveSlot(slotNumber, this.getHandle());
      }

   }

   public DisplaySlot getDisplaySlot() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      Scoreboard board = scoreboard.board;
      ScoreboardObjective objective = this.objective;

      for(int i = 0; i < 3; ++i) {
         if (board.getObjectiveForSlot(i) == objective) {
            return ScoreboardUtils.toBukkitSlot(i);
         }
      }

      return null;
   }

   public void setRenderType(RenderType renderType) throws IllegalStateException {
      Validate.notNull(renderType, "RenderType cannot be null");
      CraftScoreboard scoreboard = this.checkState();
      this.objective.setRenderType(ScoreboardUtils.fromBukkitRender(renderType));
   }

   public RenderType getRenderType() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return ScoreboardUtils.toBukkitRender(this.objective.getRenderType());
   }

   public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
      Validate.notNull(player, "Player cannot be null");
      CraftScoreboard scoreboard = this.checkState();
      return new CraftScore(this, player.getName());
   }

   public Score getScore(String entry) throws IllegalArgumentException, IllegalStateException {
      Validate.notNull(entry, "Entry cannot be null");
      Validate.isTrue(entry.length() <= 40, "Score '" + entry + "' is longer than the limit of 40 characters");
      CraftScoreboard scoreboard = this.checkState();
      return new CraftScore(this, entry);
   }

   public void unregister() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      scoreboard.board.removeObjective(this.objective);
   }

   CraftScoreboard checkState() throws IllegalStateException {
      if (this.getScoreboard().board.getObjective(this.objective.getName()) == null) {
         throw new IllegalStateException("Unregistered scoreboard component");
      } else {
         return this.getScoreboard();
      }
   }

   public int hashCode() {
      int hash = 7;
      hash = 31 * hash + (this.objective != null ? this.objective.hashCode() : 0);
      return hash;
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         CraftObjective other = (CraftObjective)obj;
         return this.objective == other.objective || this.objective != null && this.objective.equals(other.objective);
      }
   }
}
