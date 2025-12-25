package com.xyrisdev.mist.command.admin.subcommand;

import com.xyrisdev.mist.command.admin.MistCommand;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public class AboutCommand {

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(
				root.literal("about")
						.permission("mist.command.about")
						.handler(MistCommand::about)
		);
	}
}
