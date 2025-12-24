package com.xyrisdev.mist.extension.replacement.entry.type;

import com.xyrisdev.mist.extension.replacement.entry.UnifiedReplacement;
import com.xyrisdev.mist.util.text.MistTextParser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record StaticReplacement(
		@NotNull String match,
		@NotNull String replace
) implements UnifiedReplacement {

	@Override
	public @NotNull Component replacement(@NotNull Player player) {
		return MistTextParser.parse(player, replace);
	}
}
