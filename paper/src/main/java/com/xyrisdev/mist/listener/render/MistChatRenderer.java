package com.xyrisdev.mist.listener.render;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.format.entry.FormatEntry;
import com.xyrisdev.mist.util.text.TextParser;
import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MistChatRenderer {

	@SuppressWarnings("deprecation")
	public static @NotNull ChatRenderer render(@NotNull ChatContext ctx) {
		return (player, displayName, message, $$$$) -> {
			final FormatEntry entry = ctx.attributes(Key.key("mist", "render"), FormatEntry.class);

			if (entry == null) {
				return message;
			}

			Component baseComp = TextParser.parse(player, entry.message())
					.replaceText(b -> b.matchLiteral("<player>").replacement(displayName))
					.replaceText(b -> b.matchLiteral("<message>").replacement(message));

			if (!entry.hoverText().isEmpty()) {
				final List<Component> lines = new ArrayList<>(entry.hoverText().size());

				for (String line : entry.hoverText()) {
					lines.add(TextParser.parse(player, line));
				}

				final Component hover = Component.join(
						JoinConfiguration.separator(Component.newline()),
						lines
				);

				baseComp = baseComp.hoverEvent(HoverEvent.showText(hover));
			}

			if (entry.action() != null) {
				final Component parsedComp = TextParser.parse(player, entry.action().value());
				final String parsedValue = PlainTextComponentSerializer.plainText().serialize(parsedComp);

				baseComp = baseComp.clickEvent(
						ClickEvent.clickEvent(
								entry.action().action(),
								parsedValue
						)
				);
			}

			return baseComp;
		};
	}
}
