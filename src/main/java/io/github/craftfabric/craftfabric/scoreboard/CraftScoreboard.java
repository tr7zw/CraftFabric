package io.github.craftfabric.craftfabric.scoreboard;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import net.minecraft.scoreboard.ScoreboardObjective;

import com.google.common.collect.ImmutableSet.Builder;

import io.github.craftfabric.craftfabric.utility.ChatUtilities;
import io.github.craftfabric.craftfabric.utility.ScoreboardUtils;

import java.util.Iterator;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class CraftScoreboard implements Scoreboard {
   final net.minecraft.scoreboard.Scoreboard board;

   CraftScoreboard(net.minecraft.scoreboard.Scoreboard board) {
      this.board = board;
   }

   public CraftObjective registerNewObjective(String name, String criteria) throws IllegalArgumentException {
      return this.registerNewObjective(name, criteria, name);
   }

   public CraftObjective registerNewObjective(String name, String criteria, String displayName) throws IllegalArgumentException {
      return this.registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
   }

   public CraftObjective registerNewObjective(String name, String criteria, String displayName, RenderType renderType) throws IllegalArgumentException {
      Validate.notNull(name, "Objective name cannot be null");
      Validate.notNull(criteria, "Criteria cannot be null");
      Validate.notNull(displayName, "Display name cannot be null");
      Validate.notNull(renderType, "RenderType cannot be null");
      Validate.isTrue(name.length() <= 16, "The name '" + name + "' is longer than the limit of 16 characters");
      Validate.isTrue(displayName.length() <= 128, "The display name '" + displayName + "' is longer than the limit of 128 characters");
      Validate.isTrue(this.board.getObjective(name) == null, "An objective of name '" + name + "' already exists");
      CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
      ScoreboardObjective objective = this.board.addObjective(name, craftCriteria.criteria, ChatUtilities.fromStringOrNull(displayName), ScoreboardUtils.fromBukkitRender(renderType));
      return new CraftObjective(this, objective);
   }

   public Objective getObjective(String name) throws IllegalArgumentException {
      Validate.notNull(name, "Name cannot be null");
      ScoreboardObjective nms = this.board.getObjective(name);
      return nms == null ? null : new CraftObjective(this, nms);
   }

   public ImmutableSet getObjectivesByCriteria(String criteria) throws IllegalArgumentException {
      Validate.notNull(criteria, "Criteria cannot be null");
      Builder objectives = ImmutableSet.builder();
      Iterator var4 = this.board.getObjectives().iterator();

      while(var4.hasNext()) {
         ScoreboardObjective netObjective = (ScoreboardObjective)var4.next();
         CraftObjective objective = new CraftObjective(this, netObjective);
         if (objective.getCriteria().equals(criteria)) {
            objectives.add(objective);
         }
      }

      return objectives.build();
   }

   public ImmutableSet getObjectives() {
      return ImmutableSet.copyOf(Iterables.transform(this.board.getObjectives(), new CraftScoreboard$1(this)));
   }

   public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException {
      Validate.notNull(slot, "Display slot cannot be null");
      ScoreboardObjective objective = this.board.getObjectiveForSlot(ScoreboardUtils.fromBukkitSlot(slot));
      return objective == null ? null : new CraftObjective(this, objective);
   }

   public ImmutableSet getScores(OfflinePlayer player) throws IllegalArgumentException {
      Validate.notNull(player, "OfflinePlayer cannot be null");
      return this.getScores(player.getName());
   }

   public ImmutableSet getScores(String entry) throws IllegalArgumentException {
      Validate.notNull(entry, "Entry cannot be null");
      Builder scores = ImmutableSet.builder();
      Iterator var4 = this.board.getObjectives().iterator();

      while(var4.hasNext()) {
         ScoreboardObjective objective = (ScoreboardObjective)var4.next();
         scores.add(new CraftScore(new CraftObjective(this, objective), entry));
      }

      return scores.build();
   }

   public void resetScores(OfflinePlayer player) throws IllegalArgumentException {
      Validate.notNull(player, "OfflinePlayer cannot be null");
      this.resetScores(player.getName());
   }

   public void resetScores(String entry) throws IllegalArgumentException {
      Validate.notNull(entry, "Entry cannot be null");
      Iterator var3 = this.board.getObjectives().iterator();

      while(var3.hasNext()) {
         ScoreboardObjective objective = (ScoreboardObjective)var3.next();
         this.board.resetPlayerScore(entry, objective);
      }

   }

   public Team getPlayerTeam(OfflinePlayer player) throws IllegalArgumentException {
      Validate.notNull(player, "OfflinePlayer cannot be null");
      net.minecraft.scoreboard.Team team = this.board.getPlayerTeam(player.getName());
      return team == null ? null : new CraftTeam(this, team);
   }

   public Team getEntryTeam(String entry) throws IllegalArgumentException {
      Validate.notNull(entry, "Entry cannot be null");
      net.minecraft.scoreboard.Team team = this.board.getPlayerTeam(entry);
      return team == null ? null : new CraftTeam(this, team);
   }

   public Team getTeam(String teamName) throws IllegalArgumentException {
      Validate.notNull(teamName, "Team name cannot be null");
      net.minecraft.scoreboard.Team team = this.board.getTeam(teamName);
      return team == null ? null : new CraftTeam(this, team);
   }

   public ImmutableSet getTeams() {
      return ImmutableSet.copyOf(Iterables.transform(this.board.getTeams(), new CraftScoreboard$2(this)));
   }

   public Team registerNewTeam(String name) throws IllegalArgumentException {
      Validate.notNull(name, "Team name cannot be null");
      Validate.isTrue(name.length() <= 16, "Team name '" + name + "' is longer than the limit of 16 characters");
      Validate.isTrue(this.board.getTeam(name) == null, "Team name '" + name + "' is already in use");
      return new CraftTeam(this, this.board.addTeam(name));
   }

   public ImmutableSet getPlayers() {
      Builder players = ImmutableSet.builder();
      Iterator var3 = this.board.getKnownPlayers().iterator();

      while(var3.hasNext()) {
         Object playerName = var3.next();
         players.add(Bukkit.getOfflinePlayer(playerName.toString()));
      }

      return players.build();
   }

   public ImmutableSet getEntries() {
      Builder entries = ImmutableSet.builder();
      Iterator var3 = this.board.getKnownPlayers().iterator();

      while(var3.hasNext()) {
         Object entry = var3.next();
         entries.add(entry.toString());
      }

      return entries.build();
   }

   public void clearSlot(DisplaySlot slot) throws IllegalArgumentException {
      Validate.notNull(slot, "Slot cannot be null");
      this.board.setObjectiveSlot(ScoreboardUtils.fromBukkitSlot(slot), (ScoreboardObjective)null);
   }

   public net.minecraft.scoreboard.Scoreboard getHandle() {
      return this.board;
   }
   

class CraftScoreboard$1 implements Function<ScoreboardObjective, Objective> {
   // $FF: synthetic field
   final CraftScoreboard this$0;

   CraftScoreboard$1(CraftScoreboard var1) {
      this.this$0 = var1;
   }

   public Objective apply(ScoreboardObjective input) {
      return new CraftObjective(this.this$0, input);
   }
}

class CraftScoreboard$2 implements Function<net.minecraft.scoreboard.Team, Team> {
	   // $FF: synthetic field
	   final CraftScoreboard this$0;

	   CraftScoreboard$2(CraftScoreboard var1) {
	      this.this$0 = var1;
	   }

	   public Team apply(net.minecraft.scoreboard.Team input) {
	      return new CraftTeam(this.this$0, input);
	   }
	}

   
}
