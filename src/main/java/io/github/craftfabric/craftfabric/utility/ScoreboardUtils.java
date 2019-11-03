package io.github.craftfabric.craftfabric.utility;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

import com.google.common.collect.ImmutableBiMap;

import net.minecraft.scoreboard.Scoreboard;

public class ScoreboardUtils {
	static final int MAX_DISPLAY_SLOT = 3;
	static ImmutableBiMap SLOTS;

	static {
		SLOTS = ImmutableBiMap.of(DisplaySlot.BELOW_NAME, "belowName", DisplaySlot.PLAYER_LIST, "list",
				DisplaySlot.SIDEBAR, "sidebar");
	}

	public static DisplaySlot toBukkitSlot(int i) {
		return (DisplaySlot) SLOTS.inverse().get(Scoreboard.getDisplaySlotName(i));
	}

	public static int fromBukkitSlot(DisplaySlot slot) {
		return Scoreboard.getDisplaySlotId((String) SLOTS.get(slot));
	}

	public static RenderType toBukkitRender(net.minecraft.scoreboard.ScoreboardCriterion.RenderType display) {
		return RenderType.valueOf(display.name());
	}

	public static net.minecraft.scoreboard.ScoreboardCriterion.RenderType fromBukkitRender(RenderType render) {
		return net.minecraft.scoreboard.ScoreboardCriterion.RenderType.valueOf(render.name());
	}

}
