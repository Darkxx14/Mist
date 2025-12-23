package com.xyrisdev.mist.command.admin;

import com.xyrisdev.mist.command.MistCommandManager;
import com.xyrisdev.mist.command.admin.subcommands.BroadcastCommand;
import com.xyrisdev.mist.command.admin.subcommands.ChatCommand;
import com.xyrisdev.mist.command.admin.subcommands.ReloadCommand;
import com.xyrisdev.mist.command.admin.subcommands.SimilarityCommand;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;

public class MistCommand {

	public static void register() {
		final PaperCommandManager<Source> manager = MistCommandManager.manager();

		final Command.Builder<Source> root = manager.commandBuilder("mist")
				.handler(ctx ->
						ctx.sender().source().sendRichMessage(
								"<red>/mist reload | broadcast | similarity | chat"
						)
				);

		manager.command(root);

		manager.command(
				root.literal("reload")
						.permission("mist.commands.reload")
						.handler(ReloadCommand::reload)
		);

		manager.command(
				root.literal("broadcast")
						.permission("mist.commands.broadcast")
						.literal("chat")
						.required("message", StringParser.greedyStringParser())
						.handler(BroadcastCommand::chat)
		);

		manager.command(
				root.literal("broadcast")
						.permission("mist.commands.broadcast")
						.literal("title")
						.required("message", StringParser.greedyStringParser())
						.handler(BroadcastCommand::title)
		);

		manager.command(
				root.literal("broadcast")
						.permission("mist.commands.broadcast")
						.literal("actionbar")
						.required("message", StringParser.greedyStringParser())
						.handler(BroadcastCommand::actionBar)
		);

		manager.command(
				root.literal("similarity")
						.permission("mist.commands.similarity")
						.required("s1", StringParser.stringParser())
						.required("s2", StringParser.stringParser())
						.handler(SimilarityCommand::similarity)
		);

		manager.command(
				root.literal("chat")
						.permission("mist.commands.chat")
						.literal("clear")
						.handler(ChatCommand::clear)
		);

		manager.command(
				root.literal("chat")
						.permission("mist.commands.chat")
						.literal("lock")
						.handler(ChatCommand::lock)
		);

		manager.command(
				root.literal("chat")
						.permission("mist.commands.chat")
						.literal("unlock")
						.handler(ChatCommand::unlock)
		);
	}
}
