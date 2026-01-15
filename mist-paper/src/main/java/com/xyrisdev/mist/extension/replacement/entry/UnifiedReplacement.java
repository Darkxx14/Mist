package com.xyrisdev.mist.extension.replacement.entry;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface UnifiedReplacement {

	@NotNull String match();

	default boolean canApply(@NotNull Player player) {
		return true;
	}

	@NotNull
	Component replacement(@NotNull Player player);

	default Component apply(@NotNull Player player, @NotNull Component message) {
		return message.replaceText(builder ->
				builder.matchLiteral(match())
						.replacement(replacement(player))
		);
	}
}
