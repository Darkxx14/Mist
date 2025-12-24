package com.xyrisdev.mist.util.text.tags;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PlaceholderAPITag implements Modifying {

	@Override
	public @NotNull Component apply(@NotNull Component current, int depth) {
		if (depth != 0) {
			return Component.empty();
		}

		return current;
	}

	public static @NotNull TagResolver create(@NotNull Audience audience) {
		return TagResolver.resolver(Set.of("papi", "placeholderapi"), (argumentQueue, ctx) -> {
			final String placeholder = argumentQueue.popOr("The papi tag must be a placeholder.").value();

			if (audience instanceof Player) {
				final String parsed = PlaceholderAPI.setPlaceholders((Player) audience, '%' + placeholder + '%');
				return Tag.preProcessParsed(parsed);
			}

			return Tag.preProcessParsed("");
		});
	}
}