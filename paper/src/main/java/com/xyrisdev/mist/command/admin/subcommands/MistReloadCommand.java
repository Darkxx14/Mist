package com.xyrisdev.mist.command.admin.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.util.message.MistMessage;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class MistReloadCommand {

	public static LiteralCommandNode<CommandSourceStack> create() {
		return Commands.literal("reload")
				.requires(source -> source.getSender().hasPermission("mist.reload"))
				.executes(ctx -> {
					final long start = System.currentTimeMillis();

					MistPaperPlugin.instance().reload();

					final long timeTaken = System.currentTimeMillis() - start;

					MistMessage.create(ctx.getSource().getSender())
							.id("mist_reloaded")
							.placeholder("time_taken", String.valueOf(timeTaken))
							.send();

					return Command.SINGLE_SUCCESS;
				})
				.build();
	}
}