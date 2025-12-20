package com.xyrisdev.mist.command.admin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.xyrisdev.mist.command.admin.subcommands.MistBroadcastCommand;
import com.xyrisdev.mist.command.admin.subcommands.MistReloadCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class MistCommand {

	public static LiteralCommandNode<CommandSourceStack> create() {
		return Commands.literal("mist")
				.executes(ctx -> {
					return Command.SINGLE_SUCCESS;
				})
				.then(MistReloadCommand.create())
				.then(MistBroadcastCommand.create())
				.build();
	}
}
