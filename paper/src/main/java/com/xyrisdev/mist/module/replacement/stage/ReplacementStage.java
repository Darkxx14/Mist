package com.xyrisdev.mist.module.replacement.stage;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.api.processor.ChatStage;
import com.xyrisdev.mist.module.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.module.replacement.entry.PlaceholderAPIReplacement;
import com.xyrisdev.mist.util.text.MistTextParser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ReplacementStage implements ChatStage {

	private final ReplacementsConfiguration config;

	public ReplacementStage(@NotNull ReplacementsConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext context) {
		final Player player = context.player();
		Component message = context.message();

		for (Map.Entry<String, String> entry : config.replacements().entrySet()) {
			message = message.replaceText(builder ->
					builder.matchLiteral(entry.getKey())
							.replacement(MistTextParser.parse(player, entry.getValue()))
			);
		}

		for (PlaceholderAPIReplacement replacement : config.papiReplacements().values()) {
			if (replacement.requirePermission() && !player.hasPermission(replacement.permission())) {
				continue;
			}

			message = message.replaceText(builder ->
					builder.matchLiteral(replacement.key())
							.replacement(MistTextParser.parse(player, replacement.processor()))
			);
		}

		context.message(message);
	}
}
