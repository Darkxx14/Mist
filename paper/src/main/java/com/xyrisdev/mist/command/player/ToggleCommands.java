package com.xyrisdev.mist.command.player;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.api.chat.user.toggle.ChatSettingType;
import com.xyrisdev.mist.user.ChatUserManager;
import com.xyrisdev.mist.util.command.ConfigurableCommand;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ToggleCommands {

	public static void register() {
		command("toggleglobalchat", ChatSettingType.GLOBAL_CHAT, "toggle_global_chat");
		command("togglepm", ChatSettingType.PRIVATE_MESSAGES, "toggle_private_messages");
		command("togglementions", ChatSettingType.MENTIONS, "toggle_mentions");
		command("toggleannouncements", ChatSettingType.ANNOUNCEMENTS, "toggle_announcements");
	}

	private static void command(@NotNull String key, @NotNull ChatSettingType type, @NotNull String msgId) {
		ConfigurableCommand.register(
				key,
				true,
				builder -> builder.handler(ctx -> toggle(ctx, type, msgId))
		);
	}

	private static void toggle(@NotNull CommandContext<Source> ctx, @NotNull ChatSettingType type, @NotNull String msgId) {
		final PlayerSource sender = (PlayerSource) ctx.sender();
		final UUID id = sender.source().getUniqueId();

		final ChatUserManager userManager = ChatPlugin.instance().userManager();

		final boolean enable = !userManager.get(id).settings().enabled(type);

		userManager.modify(id, user -> {
			if (user.settings().enabled(type)) {
				user.settings().disable(type);
			} else {
				user.settings().enable(type);
			}
		});

		MistMessage.create(sender.source())
				.id(msgId)
				.placeholder("state", enable ? "enabled" : "disabled")
				.send();
	}
}

