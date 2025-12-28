package com.xyrisdev.mist.command.player;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.user.ChatUserManager;
import com.xyrisdev.mist.util.command.ConfigurableCommand;
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

		final ChatUserManager users = ChatPlugin.instance().userManager();

		final OfflinePlayer target = ctx.get("target");

		if (!target.hasPlayedBefore()) {
			sender.source().sendRichMessage("<red>That player has never joined the server.");
			return;
		}

		final UUID targetId = target.getUniqueId();

		if (targetId.equals(senderId)) {
			sender.source().sendRichMessage("<red>You cannot ignore yourself.");
			return;
		}

		users.modify(sender.source(), user -> {
			if (user.ignore().contains(targetId)) {
				user.ignore().remove(targetId);
				sender.source().sendRichMessage("<green>You are no longer ignoring <white>" + target.getName() + "</white>.");
			} else {
				user.ignore().add(targetId);
				sender.source().sendRichMessage("<yellow>You are now ignoring <white>" + target.getName() + "</white>.");
			}
		});
	}

	private static @NotNull CompletableFuture<Iterable<Suggestion>> suggestions(@NotNull CommandContext<?> ctx, @NotNull CommandInput input) {
		return CompletableFuture.supplyAsync(() -> {
			final Source source = (Source) ctx.sender();
			final PlayerSource sender = (PlayerSource) source;
			final UUID senderId = sender.source().getUniqueId();

			final ChatUser user = ChatPlugin.instance()
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
		}, MistExecutors.processor()::execute);
	}
}