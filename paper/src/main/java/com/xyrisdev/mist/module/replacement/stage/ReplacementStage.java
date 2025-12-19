package com.xyrisdev.mist.module.replacement.stage;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.api.processor.ChatStage;
import com.xyrisdev.mist.module.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.module.replacement.entry.UnifiedReplacement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ReplacementStage implements ChatStage {

	private final ReplacementsConfiguration config;

	public ReplacementStage(@NotNull ReplacementsConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext context) {
		final Player player = context.player();
		Component message = context.message();

		for (UnifiedReplacement replacement : config.replacements()) {

			if (!replacement.canApply(player)) {
				continue;
			}

			message = replacement.apply(player, message);
		}

		context.message(message);
	}
}
