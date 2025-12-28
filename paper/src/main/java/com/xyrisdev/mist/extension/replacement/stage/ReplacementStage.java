package com.xyrisdev.mist.extension.replacement.stage;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.extension.replacement.entry.UnifiedReplacement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReplacementStage implements ChatProcessStage {

	private final ReplacementsConfiguration config;

	public ReplacementStage(@NotNull ReplacementsConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext ctx) {
		final Player player = ctx.sender();
		Component message = ctx.message();

		for (UnifiedReplacement replacement : this.config.replacements()) {

			if (!replacement.canApply(player)) {
				continue;
			}

			message = replacement.apply(player, message);
		}

		ctx.message(message);
	}
}
