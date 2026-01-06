package com.xyrisdev.mist.command.player;

import com.xyrisdev.mist.util.command.ConfigurableCommand;
import org.bukkit.entity.Player;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;
import org.jetbrains.annotations.NotNull;

public class MessageCommand {

	public static void register() {
		ConfigurableCommand.register(
				"message",
				true,
				builder -> builder
						.argument(PlayerParser.playerComponent().name("target"))
						.required("message", StringParser.greedyStringParser())
						.handler(MessageCommand::message)
		);

		ConfigurableCommand.register(
				"reply",
				true,
				builder -> builder
						.required("message", StringParser.greedyStringParser())
						.handler(MessageCommand::reply)
		);
	}

	private static void message(@NotNull CommandContext<Source> ctx) {
		final Player sender = ((PlayerSource) ctx.sender()).source();
		final Player target = ctx.get("target");
		final String message = ctx.get("message");

		//todo: impl fun
	}

	private static void reply(@NotNull CommandContext<Source> ctx) {
		final Player sender = ((PlayerSource) ctx.sender()).source();
		final String message = ctx.get("message");

		//todo: impl fun
	}
}
