package com.xyrisdev.mist.command.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.util.styling.MistLayout;
import com.xyrisdev.mist.util.styling.layout.LineStyle;
import com.xyrisdev.mist.util.styling.layout.LineType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public final class MistReloadCommand {

	public static LiteralCommandNode<CommandSourceStack> create() {
		return Commands.literal("reload")
				.requires(source -> source.getSender().hasPermission("mist.reload"))
				.executes(ctx -> {
					final long startTime = System.nanoTime();

					MistPaperPlugin.instance().reload();

					final long endTime = System.nanoTime();
					double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

					MistLayout.create(ctx.getSource().getSender())
							.blank()
							.header()
							.version()

							.line(
									LineType.CENTER,
									MistLayout.part(LineStyle.SUCCESS, "Successfully reloaded all configuration files.")
							)
							.line(
									LineType.CENTER,
									MistLayout.part(LineStyle.INFO, String.format("<smallcaps>Completed in %.3f seconds</smallcaps>", elapsedSeconds))
							)

							.blank()
							.send();

					return Command.SINGLE_SUCCESS;
				})
				.build();
	}
}