package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.command.subcommand.ChatCommand;
import com.xyrisdev.mist.listener.render.MistChatRenderer;
import com.xyrisdev.mist.util.message.MistMessage;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
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

					final ChatContext ctx = new ChatContext(
							event.getPlayer(),
							event.message()
					);

					ChatPlugin.instance()
							.chatProcessor()
							.process(ctx);

					if (ctx.result() == ChatResult.CANCEL) {
						event.setCancelled(true);
						return;
					}

					event.message(ctx.message());

					event.viewers().removeIf(audience -> {
						if (!(audience instanceof Player player)) {
							return false;
						}

						final ChatUser user = ChatPlugin.instance()
											 .userManager()
											 .get(player.getUniqueId());

						return user == null || !user.settings().globalChat();
					});

					event.renderer(MistChatRenderer.render(ctx));
				})
				.build();
	}
}
