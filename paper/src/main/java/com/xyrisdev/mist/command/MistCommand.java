package com.xyrisdev.mist.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.xyrisdev.mist.command.subcommands.MistBroadcastCommand;
import com.xyrisdev.mist.command.subcommands.MistReloadCommand;
import com.xyrisdev.mist.util.styling.MistLayout;
import com.xyrisdev.mist.util.styling.layout.LineStyle;
import com.xyrisdev.mist.util.styling.layout.LineType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public final class MistCommand {

	public static LiteralCommandNode<CommandSourceStack> mist() {
		return Commands.literal("mist")
				.executes(ctx -> {
					MistLayout.create(ctx.getSource().getSender())
							.blank()
							.header()
							.version()

							.line(
									LineType.CENTER,
									MistLayout.part(LineStyle.GRADIENT, "/mist reload")
							)
							.line(
									LineType.CENTER,
									MistLayout.part(LineStyle.GRADIENT, "/mist broadcast chat "),
									MistLayout.part(LineStyle.INFO, "<message>")
							)
							.line(
									LineType.CENTER,
									MistLayout.part(LineStyle.GRADIENT, "/mist broadcast title "),
									MistLayout.part(LineStyle.INFO, "<title:[subtitle]>")
							)
							.line(
									LineType.CENTER,
									MistLayout.part(LineStyle.GRADIENT, "/mist broadcast actionbar "),
									MistLayout.part(LineStyle.INFO, "<message>")
							)

							.blank()
							.send();

					return Command.SINGLE_SUCCESS;
				})
				.then(MistReloadCommand.create())
				.then(MistBroadcastCommand.create())
				.build();
	}
}
