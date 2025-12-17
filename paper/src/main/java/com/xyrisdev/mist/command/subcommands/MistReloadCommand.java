package com.xyrisdev.mist.command.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
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
					MistLayout.create(ctx.getSource().getSender())
							.blank()
							.header()
							.version()

							.line(
									LineType.CENTER,
									MistLayout.part(LineStyle.SUCCESS, "Successfully reloaded all configuration files.")
							)

							.blank()
							.send();

					return Command.SINGLE_SUCCESS;
				})
				.build();
	}
}
