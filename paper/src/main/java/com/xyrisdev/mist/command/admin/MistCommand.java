package com.xyrisdev.mist.command.admin;

import com.xyrisdev.mist.command.MistCommandManager;
import com.xyrisdev.mist.command.admin.subcommand.*;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

public final class MistCommand {

	public static void register() {
		final PaperCommandManager<Source> manager = MistCommandManager.manager();

		final Command.Builder<Source> root = manager.commandBuilder("mist")
				.handler(ctx -> ctx.sender().source().sendRichMessage(
						"<red>/mist reload | broadcast | similarity | chat | regex"
				));

		manager.command(root);

		new ReloadCommand().register(manager, root);
		new BroadcastCommand().register(manager, root);
		new SimilarityCommand().register(manager, root);
		new ChatCommand().register(manager, root);
		new RegexCommand().register(manager, root);
	}
}
