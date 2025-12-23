package com.xyrisdev.mist.command.admin.subcommands;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand {

	public static void reload(@NotNull CommandContext<Source> ctx) {
		final long start = System.currentTimeMillis();

		MistPaperPlugin.instance().reload();

		MistMessage.create(ctx.sender().source())
				.id("mist_reloaded")
				.placeholder(
						"time_taken",
						String.valueOf(System.currentTimeMillis() - start)
				)
				.send();
	}
}
