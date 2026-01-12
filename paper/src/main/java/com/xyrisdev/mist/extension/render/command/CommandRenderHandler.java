package com.xyrisdev.mist.extension.render.command;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.render.common.RenderHandler;
import com.xyrisdev.mist.extension.render.config.RenderConfiguration;
import com.xyrisdev.mist.util.text.TextParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandRenderHandler implements RenderHandler {

	private final RenderConfiguration config;

	public CommandRenderHandler(@NotNull RenderConfiguration config) {
		this.config = config;
	}

	@Override
	public void handle(@NotNull ChatContext ctx) {
		final RenderConfiguration.SectionConfig section = config.command();

		if (!section.enabled()) {
			return;
		}

		final String message = ctx.plain();
		final Player player = ctx.sender();

		for (String pattern : section.prefix()) {
			final int cmdIndex = pattern.indexOf("$cmd");

			if (cmdIndex == -1) {
				continue;
			}

			final String before = pattern.substring(0, cmdIndex);
			final String after = pattern.substring(cmdIndex + "$cmd".length());

			final int start = message.indexOf(before);

			if (start == -1) {
				continue;
			}

			final int from = start + before.length();
			final int end = message.indexOf(after, from);

			if (end == -1) {
				continue;
			}

			final String fullCmd = message.substring(from, end).trim();

			if (fullCmd.isEmpty()) {
				continue;
			}

			final String baseCmd = fullCmd.split(" ", 2)[0];

			final String processed = section.processor()
					.replace("<cmd>", baseCmd)
					.replace("<full_cmd>", fullCmd);

			Component comp = TextParser.parse(player, processed).clickEvent(ClickEvent.suggestCommand(fullCmd));

			if (!section.hoverText().isEmpty()) {
				comp = comp.hoverEvent(HoverEvent.showText(
						Component.join(
								JoinConfiguration.separator(Component.newline()),
								section.hoverText().stream()
										.map(line -> TextParser.parse(
												player,
												line
														.replace("<cmd>", baseCmd)
														.replace("<full_cmd>", fullCmd)
										))
										.toList()
						)
				));
			}

			final String raw = before + fullCmd + after;

			Component finalComp = comp;

			ctx.message(ctx.message().replaceText(builder -> builder.matchLiteral(raw).replacement(finalComp)));
		}
	}
}
