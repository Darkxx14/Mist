package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.command.subcommand.ChatCommand;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.listener.render.MistChatRenderer;
import com.xyrisdev.mist.util.message.MistMessage;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

public final class AsyncChatListener {

	public static @NotNull EventHandler<AsyncChatEvent> listener() {
		return EventBuilder.event(AsyncChatEvent.class)
				.execute(event -> {
							final Player sender = event.getPlayer();

							if (ChatCommand.getLocked() && !sender.hasPermission("mist.chat.bypass")) {
								event.setCancelled(true);

								MistMessage.create(sender)
										.id("chat_locked_blocked")
										.send();
								return;
							}

							final ChatContext ctx = new ChatContext(
									sender,
									event.message()
							);

							Mist.INSTANCE.chatProcessor().process(ctx);

							if (ctx.result() == ChatResult.CANCEL) {
								event.setCancelled(true);
								return;
							}

							event.message(ctx.message());

							final boolean hideIgnored = Mist.INSTANCE.config()
													   .get(ConfigType.CONFIGURATION)
													   .get("ignoring.hide_chat_messages", true);

							event.viewers().removeIf(audience -> {
								if (!(audience instanceof Player viewer)) {
									return false;
								}

								final ChatUser user = Mist.INSTANCE.userManager().get(viewer.getUniqueId());

								return user == null || !user.settings().globalChat()
										|| (hideIgnored && user.ignore().contains(sender.getUniqueId()));
							});

							event.renderer(MistChatRenderer.renderer(ctx));

							final Component rendered = MistChatRenderer.render(
									ctx,
									sender,
									sender.displayName(),
									event.message()
							);

							Mist.INSTANCE.sync().publish(
									Mist.INSTANCE.config().get(ConfigType.CONFIGURATION).getString("chat_synchronization.server_id", "server1"),
									sender.getUniqueId(),
									GsonComponentSerializer.gson().serialize(rendered)
							);

						}
				)
				.build().priority(EventPriority.LOWEST);
	}
}
