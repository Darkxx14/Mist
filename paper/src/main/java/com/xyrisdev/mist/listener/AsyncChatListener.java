package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.command.admin.subcommands.ChatCommand;
import com.xyrisdev.mist.listener.render.route.RoutedChatRenderer;
import com.xyrisdev.mist.util.message.MistMessage;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.jetbrains.annotations.NotNull;

public class AsyncChatListener {

	public static @NotNull EventHandler<AsyncChatEvent> listener() {
		return EventBuilder.event(AsyncChatEvent.class)
				.execute(event -> {
					// chat lock - start
					if (ChatCommand.getLocked() && !event.getPlayer().hasPermission("mist.chat.bypass")) {
						event.setCancelled(true);

						MistMessage.create(event.getPlayer())
								.id("chat_locked_blocked")
								.send();

						return;
					}
					// chat lock - end

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
							RoutedChatRenderer.route(context)
					);

				})
				.build();
	}
}
