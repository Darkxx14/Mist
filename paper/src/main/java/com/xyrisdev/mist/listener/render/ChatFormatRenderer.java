package com.xyrisdev.mist.listener.render;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.format.entry.FormatEntry;
import com.xyrisdev.mist.util.text.MistTextParser;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class ChatFormatRenderer {

	@SuppressWarnings("deprecation")
	public static @NotNull ChatRenderer.ViewerUnaware render(@NotNull ChatContext context) {
		return (player, displayName, message) -> {
			final FormatEntry entry = context.data(Key.key("mist", "render"), FormatEntry.class);

			if (entry == null) {
				return message;
			}

			Component base = MistTextParser.parse(player, entry.message())
					.replaceText(b -> b.matchLiteral("<player>").replacement(displayName))
					.replaceText(b -> b.matchLiteral("<message>").replacement(message));

			if (!entry.hoverText().isEmpty()) {
				final Component hover = Component.join(
						JoinConfiguration.separator(Component.newline()),
						entry.hoverText().stream()
								.map(line -> MistTextParser.parse(player, line))
								.toList()
				);

				base = base.hoverEvent(HoverEvent.showText(hover));
			}

			if (entry.action() != null) {
				final Component parsedComponent = MistTextParser.parse(player, entry.action().value());
				final String parsedValue = PlainTextComponentSerializer.plainText().serialize(parsedComponent);

				base = base.clickEvent(
						ClickEvent.clickEvent(
								entry.action().action(),
								parsedValue
						)
				);
			}

			return base;
		};
	}
}
