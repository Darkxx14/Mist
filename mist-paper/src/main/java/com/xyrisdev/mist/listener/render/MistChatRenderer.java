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
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MistChatRenderer {

	@SuppressWarnings("deprecation")
	public static @NotNull ChatRenderer render(@NotNull ChatContext ctx) {
		return (player, displayName, message, audience) -> {
			final FormatEntry entry = ctx.attributes(Key.key("mist", "render"), FormatEntry.class);

			if (entry == null) {
				return message;
			}

			final String name = PlainTextComponentSerializer.plainText().serialize(displayName);

			Component result = TextParser.parse(
					player,
					entry.message().replace("<player>", name),
					Placeholder.component("message", message)
			);

			if (!entry.hoverText().isEmpty()) {
				final List<Component> lines = new ArrayList<>(entry.hoverText().size());

				for (String line : entry.hoverText()) {
					lines.add(TextParser.parse(player, line));
				}

				result = result.hoverEvent(HoverEvent.showText(
						Component.join(
								JoinConfiguration.separator(Component.newline()),
								lines
						))
				);
			}

			if (entry.action() != null) {
				final String value = PlainTextComponentSerializer.plainText().serialize(TextParser.parse(player, entry.action().value()));

				result = result.clickEvent(
						ClickEvent.clickEvent(
								entry.action().action(),
								value
						)
				);
			}

			return result;
		};
	}
}