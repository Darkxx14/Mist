package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.context.ChatContext;
import io.papermc.paper.event.player.AsyncChatEvent;
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
				})
				.build();
	}
}
