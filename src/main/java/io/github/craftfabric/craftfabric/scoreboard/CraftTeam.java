package io.github.craftfabric.craftfabric.scoreboard;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import io.github.craftfabric.craftfabric.utility.ChatUtilities;
import net.minecraft.scoreboard.AbstractTeam.CollisionRule;
import net.minecraft.scoreboard.AbstractTeam.VisibilityRule;

import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

final class CraftTeam extends CraftScoreboardComponent implements Team {
   private final net.minecraft.scoreboard.Team team;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$org$bukkit$scoreboard$Team$Option;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility;

   CraftTeam(CraftScoreboard scoreboard, net.minecraft.scoreboard.Team team) {
      super(scoreboard);
      this.team = team;
   }

   public String getName() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return this.team.getName();
   }

   public String getDisplayName() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return ChatUtilities.fromComponent(this.team.getDisplayName());
   }

   public void setDisplayName(String displayName) throws IllegalStateException {
      Validate.notNull(displayName, "Display name cannot be null");
      Validate.isTrue(displayName.length() <= 128, "Display name '" + displayName + "' is longer than the limit of 128 characters");
      CraftScoreboard scoreboard = this.checkState();
      this.team.setDisplayName(ChatUtilities.fromString(displayName)[0]);
   }

   public String getPrefix() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return ChatUtilities.fromComponent(this.team.getPrefix());
   }

   public void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException {
      Validate.notNull(prefix, "Prefix cannot be null");
      Validate.isTrue(prefix.length() <= 64, "Prefix '" + prefix + "' is longer than the limit of 64 characters");
      CraftScoreboard scoreboard = this.checkState();
      this.team.setPrefix(ChatUtilities.fromStringOrNull(prefix));
   }

   public String getSuffix() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return ChatUtilities.fromComponent(this.team.getSuffix());
   }

   public void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException {
      Validate.notNull(suffix, "Suffix cannot be null");
      Validate.isTrue(suffix.length() <= 64, "Suffix '" + suffix + "' is longer than the limit of 64 characters");
      CraftScoreboard scoreboard = this.checkState();
      this.team.setSuffix(ChatUtilities.fromStringOrNull(suffix));
   }

   public ChatColor getColor() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return ChatUtilities.getColor(this.team.getColor());
   }

   public void setColor(ChatColor color) {
      Validate.notNull(color, "Color cannot be null");
      CraftScoreboard scoreboard = this.checkState();
      this.team.setColor(ChatUtilities.getColor(color));
   }

   public boolean allowFriendlyFire() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return this.team.isFriendlyFireAllowed();
   }

   public void setAllowFriendlyFire(boolean enabled) throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      this.team.setFriendlyFireAllowed(enabled);
   }

   public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return this.team.shouldShowFriendlyInvisibles();
   }

   public void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      this.team.setShowFriendlyInvisibles(enabled);
   }

   public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
      CraftScoreboard scoreboard = this.checkState();
      return notchToBukkit(this.team.getNameTagVisibilityRule());
   }

   public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
      CraftScoreboard scoreboard = this.checkState();
      this.team.setNameTagVisibilityRule(bukkitToNotch(visibility));
   }

   public Set getPlayers() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      Builder players = ImmutableSet.builder();
      Iterator var4 = this.team.getPlayerList().iterator();

      while(var4.hasNext()) {
         String playerName = (String)var4.next();
         players.add(Bukkit.getOfflinePlayer(playerName));
      }

      return players.build();
   }

   public Set getEntries() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      Builder entries = ImmutableSet.builder();
      Iterator var4 = this.team.getPlayerList().iterator();

      while(var4.hasNext()) {
         String playerName = (String)var4.next();
         entries.add(playerName);
      }

      return entries.build();
   }

   public int getSize() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      return this.team.getPlayerList().size();
   }

   public void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
      Validate.notNull(player, "OfflinePlayer cannot be null");
      this.addEntry(player.getName());
   }

   public void addEntry(String entry) throws IllegalStateException, IllegalArgumentException {
      Validate.notNull(entry, "Entry cannot be null");
      CraftScoreboard scoreboard = this.checkState();
      scoreboard.board.addPlayerToTeam(entry, this.team);
   }

   public boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
      Validate.notNull(player, "OfflinePlayer cannot be null");
      return this.removeEntry(player.getName());
   }

   public boolean removeEntry(String entry) throws IllegalStateException, IllegalArgumentException {
      Validate.notNull(entry, "Entry cannot be null");
      CraftScoreboard scoreboard = this.checkState();
      if (!this.team.getPlayerList().contains(entry)) {
         return false;
      } else {
         scoreboard.board.removePlayerFromTeam(entry, this.team);
         return true;
      }
   }

   public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
      Validate.notNull(player, "OfflinePlayer cannot be null");
      return this.hasEntry(player.getName());
   }

   public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException {
      Validate.notNull("Entry cannot be null");
      CraftScoreboard scoreboard = this.checkState();
      return this.team.getPlayerList().contains(entry);
   }

   public void unregister() throws IllegalStateException {
      CraftScoreboard scoreboard = this.checkState();
      scoreboard.board.removeTeam(this.team);
   }

   public OptionStatus getOption(Option option) throws IllegalStateException {
      this.checkState();
      switch($SWITCH_TABLE$org$bukkit$scoreboard$Team$Option()[option.ordinal()]) {
      case 1:
         return OptionStatus.values()[this.team.getNameTagVisibilityRule().ordinal()];
      case 2:
         return OptionStatus.values()[this.team.getDeathMessageVisibilityRule().ordinal()];
      case 3:
         return OptionStatus.values()[this.team.getCollisionRule().ordinal()];
      default:
         throw new IllegalArgumentException("Unrecognised option " + option);
      }
   }

   public void setOption(Option option, OptionStatus status) throws IllegalStateException {
      this.checkState();
      switch($SWITCH_TABLE$org$bukkit$scoreboard$Team$Option()[option.ordinal()]) {
      case 1:
         this.team.setNameTagVisibilityRule(VisibilityRule.values()[status.ordinal()]);
         break;
      case 2:
         this.team.setDeathMessageVisibilityRule(VisibilityRule.values()[status.ordinal()]);
         break;
      case 3:
         this.team.setCollisionRule(CollisionRule.values()[status.ordinal()]);
         break;
      default:
         throw new IllegalArgumentException("Unrecognised option " + option);
      }

   }

   public static VisibilityRule bukkitToNotch(NameTagVisibility visibility) {
      switch($SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility()[visibility.ordinal()]) {
      case 1:
         return VisibilityRule.ALWAYS;
      case 2:
         return VisibilityRule.NEVER;
      case 3:
         return VisibilityRule.HIDE_FOR_OTHER_TEAMS;
      case 4:
         return VisibilityRule.HIDE_FOR_OWN_TEAM;
      default:
         throw new IllegalArgumentException("Unknown visibility level " + visibility);
      }
   }

   public static NameTagVisibility notchToBukkit(VisibilityRule visibility) {
      switch($SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility()[visibility.ordinal()]) {
      case 1:
         return NameTagVisibility.ALWAYS;
      case 2:
         return NameTagVisibility.NEVER;
      case 3:
         return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
      case 4:
         return NameTagVisibility.HIDE_FOR_OWN_TEAM;
      default:
         throw new IllegalArgumentException("Unknown visibility level " + visibility);
      }
   }

   CraftScoreboard checkState() throws IllegalStateException {
      if (this.getScoreboard().board.getTeam(this.team.getName()) == null) {
         throw new IllegalStateException("Unregistered scoreboard component");
      } else {
         return this.getScoreboard();
      }
   }

   public int hashCode() {
      int hash = 7;
      hash = 43 * hash + (this.team != null ? this.team.hashCode() : 0);
      return hash;
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         CraftTeam other = (CraftTeam)obj;
         return this.team == other.team || this.team != null && this.team.equals(other.team);
      }
   }

   static int[] $SWITCH_TABLE$org$bukkit$scoreboard$Team$Option() {
      int[] var10000 = $SWITCH_TABLE$org$bukkit$scoreboard$Team$Option;
      if ($SWITCH_TABLE$org$bukkit$scoreboard$Team$Option != null) {
         return var10000;
      } else {
         int[] var0 = new int[Option.values().length];

         try {
            var0[Option.COLLISION_RULE.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[Option.DEATH_MESSAGE_VISIBILITY.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[Option.NAME_TAG_VISIBILITY.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$org$bukkit$scoreboard$Team$Option = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility() {
      int[] var10000 = $SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility;
      if ($SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility != null) {
         return var10000;
      } else {
         int[] var0 = new int[NameTagVisibility.values().length];

         try {
            var0[NameTagVisibility.ALWAYS.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[NameTagVisibility.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[NameTagVisibility.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[NameTagVisibility.NEVER.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$org$bukkit$scoreboard$NameTagVisibility = var0;
         return var0;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility;
      if ($SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility != null) {
         return var10000;
      } else {
         int[] var0 = new int[VisibilityRule.values().length];

         try {
            var0[VisibilityRule.ALWAYS.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[VisibilityRule.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[VisibilityRule.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[VisibilityRule.NEVER.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$net$minecraft$server$ScoreboardTeamBase$EnumNameTagVisibility = var0;
         return var0;
      }
   }
}
