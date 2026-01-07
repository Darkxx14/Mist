package com.xyrisdev.mist.command.player;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.user.ChatUserManager;
import com.xyrisdev.mist.util.command.ConfigurableCommand;
import com.xyrisdev.mist.util.message.MistMessage;
import com.xyrisdev.mist.util.thread.MistExecutors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.suggestion.Suggestion;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class IgnoreCommand {

	public static void register() {
		ConfigurableCommand.register(
				"ignore",
				true,
				builder -> builder
						.argument(
								OfflinePlayerParser
										.offlinePlayerComponent()
										.name("target")
										.suggestionProvider(IgnoreCommand::suggestions)
						)
						.handler(IgnoreCommand::ignore)
		);
	}


	private static void ignore(@NotNull CommandContext<Source> ctx) {
		final PlayerSource sender = (PlayerSource) ctx.sender();
		final UUID senderId = sender.source().getUniqueId();

		final ChatUserManager users = ChatPlugin.service().userManager();

		final OfflinePlayer target = ctx.get("target");

		if (!target.hasPlayedBefore()) {
			MistMessage.create(sender.source())
					.id("ignore_never_joined")
					.send();
			return;
		}

		final UUID targetId = target.getUniqueId();

		if (targetId.equals(senderId)) {
			MistMessage.create(sender.source())
					.id("ignore_self")
					.send();
			return;
		}

		users.modify(sender.source(), user -> {
			if (user.ignore().contains(targetId)) {
				user.ignore().remove(targetId);

				MistMessage.create(sender.source())
						.id("ignore_removed")
						.placeholder("player", target.getName())
						.send();
			} else {
				user.ignore().add(targetId);

				MistMessage.create(sender.source())
						.id("ignore_added")
						.placeholder("player", target.getName())
						.send();
			}
		});
	}

	private static @NotNull CompletableFuture<Iterable<Suggestion>> suggestions(@NotNull CommandContext<?> ctx, @NotNull CommandInput input) {
		return CompletableFuture.supplyAsync(() -> {
			final Source source = (Source) ctx.sender();
			final PlayerSource sender = (PlayerSource) source;
			final UUID senderId = sender.source().getUniqueId();

			final ChatUser user = ChatPlugin.service()
								 .userManager()
								 .get(senderId);

			final String token = input.lastRemainingToken();

			return user.ignore()
					.snapshot()
					.stream()
					.map(Bukkit::getOfflinePlayer)
					.filter(OfflinePlayer::hasPlayedBefore)
					.map(OfflinePlayer::getName)
					.filter(name -> name != null && name.regionMatches(true, 0, token, 0, token.length()))
					.map(Suggestion::suggestion)
					.toList();
		}, MistExecutors.processor());
	}
}