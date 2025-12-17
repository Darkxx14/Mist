package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.format.entry.FormatEntry;
import com.xyrisdev.mist.util.text.MistTextParser;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

public final class AsyncChatListener {

	public static @NotNull EventHandler<AsyncChatEvent> listener() {
		return EventBuilder.event(AsyncChatEvent.class)
				.execute(event -> {
					final ChatContext context = new ChatContext(
							event.getPlayer(),
							event.message()
					);

					MistPaperPlugin.instance()
							.chatProcessor()
							.process(context);

					if (context.cancelled()) {
						event.setCancelled(true);
						return;
					}

					event.message(context.message());

					event.renderer(
							ChatRenderer.viewerUnaware((player, displayName, message) -> {
								final FormatEntry entry = context.format(FormatEntry.class);

								if (entry == null) {
									return message;
								}

								Component base = MistTextParser.parse(player, entry.message())
										.replaceText(b -> b.matchLiteral("<player>").replacement(displayName))
										.replaceText(b -> b.matchLiteral("<message>").replacement(message));

								if (!entry.hoverText().isEmpty()) {
									Component hover = Component.join(
											Component.newline(),
											entry.hoverText().stream()
													.map(line -> MistTextParser.parse(player, line))
													.toList()
									);
									base = base.hoverEvent(HoverEvent.showText(hover));
								}

								if (entry.action() != null) {
									base = base.clickEvent(
											ClickEvent.clickEvent(
													entry.action().action(),
													entry.action().value()
											)
									);
								}

								return base;
							})
					);

				})
				.build();
	}
}
