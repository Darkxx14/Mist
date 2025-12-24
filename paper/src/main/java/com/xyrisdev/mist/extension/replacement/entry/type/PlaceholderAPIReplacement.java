package com.xyrisdev.mist.extension.replacement.entry.type;

import com.xyrisdev.mist.extension.replacement.entry.UnifiedReplacement;
import com.xyrisdev.mist.util.text.MistTextParser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record PlaceholderAPIReplacement(
		@NotNull String match,
		@NotNull String processor,
		@NotNull String permission
) implements UnifiedReplacement {

	@Override
	public boolean canApply(@NotNull Player player) {
		return permission.isEmpty() || player.hasPermission(permission);
	}

	@Override
	public @NotNull Component replacement(@NotNull Player player) {
		return MistTextParser.parse(player, processor);
	}
}
