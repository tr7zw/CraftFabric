package io.github.craftfabric.craftfabric.scoreboard;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

final class CraftCriteria {
   static final Map DEFAULTS;
   static final CraftCriteria DUMMY;
   final ScoreboardCriterion criteria;
   final String bukkitName;

   static {
      Builder defaults = ImmutableMap.builder();
      Iterator var2 = ScoreboardCriterion.OBJECTIVES.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         String name = entry.getKey().toString();
         ScoreboardCriterion criteria = (ScoreboardCriterion)entry.getValue();
         defaults.put(name, new CraftCriteria(criteria));
      }

      DEFAULTS = defaults.build();
      DUMMY = (CraftCriteria)DEFAULTS.get("dummy");
   }

   private CraftCriteria(String bukkitName) {
      this.bukkitName = bukkitName;
      this.criteria = DUMMY.criteria;
   }

   private CraftCriteria(ScoreboardCriterion criteria) {
      this.criteria = criteria;
      this.bukkitName = criteria.getName();
   }

   static CraftCriteria getFromNMS(ScoreboardObjective objective) {
      return (CraftCriteria)DEFAULTS.get(objective.getCriterion().getName());
   }

   static CraftCriteria getFromBukkit(String name) {
      CraftCriteria criteria = (CraftCriteria)DEFAULTS.get(name);
      return criteria != null ? criteria : new CraftCriteria(name);
   }

   public boolean equals(Object that) {
      return !(that instanceof CraftCriteria) ? false : ((CraftCriteria)that).bukkitName.equals(this.bukkitName);
   }

   public int hashCode() {
      return this.bukkitName.hashCode() ^ CraftCriteria.class.hashCode();
   }
}
