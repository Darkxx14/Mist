package com.xyrisdev.mist.command.internal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.xyrisdev.mist.module.render.impl.RenderService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class MistCallbackCommand {

	public static LiteralCommandNode<CommandSourceStack> create() {
		return Commands.literal("mistcallback")
				.then(Commands.argument("id", ArgumentTypes.uuid())
						.executes(ctx -> {
							if (!(ctx.getSource().getSender() instanceof Player player)) {
								return Command.SINGLE_SUCCESS;
							}

							final UUID id = ctx.getArgument("id", UUID.class);
							RenderService.get().render(player, id);

							return Command.SINGLE_SUCCESS;
						})
				)

				.build();
	}
}
