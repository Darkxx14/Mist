package com.xyrisdev.mist.command.admin.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.xyrisdev.mist.util.text.MistTextParser;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;

import java.time.Duration;

public class MistBroadcastCommand {

	public static LiteralCommandNode<CommandSourceStack> create() {
		return Commands.literal("broadcast")
				.requires(source -> source.getSender().hasPermission("mist.broadcast"))

				.then(Commands.literal("chat")
						.then(Commands.argument("message", StringArgumentType.greedyString())
								.executes(ctx -> {
									final String input = ctx.getArgument("message", String.class);

									Bukkit.getOnlinePlayers().forEach(player ->
											player.sendMessage(
													MistTextParser.parse(player, input)
											)
									);

									return Command.SINGLE_SUCCESS;
								})
						)
				)

				.then(Commands.literal("title")
						.then(Commands.argument("message", StringArgumentType.greedyString())
								.executes(ctx -> {
									final String input = ctx.getArgument("message", String.class);
									final String[] parts = input.split(":", 2);

									Bukkit.getOnlinePlayers().forEach(player -> {
										final Component title = MistTextParser.parse(player, parts[0]);
										final Component subtitle = parts.length > 1
												? MistTextParser.parse(player, parts[1])
												: Component.empty();

										player.showTitle(
												Title.title(
														title,
														subtitle,
														Title.Times.times(
																Duration.ofMillis(300),
																Duration.ofSeconds(2),
																Duration.ofMillis(300)
														)
												)
										);
									});

									return Command.SINGLE_SUCCESS;
								})
						)
				)

				.then(Commands.literal("actionbar")
						.then(Commands.argument("message", StringArgumentType.greedyString())
								.executes(ctx -> {
									final String input = ctx.getArgument("message", String.class);

									Bukkit.getOnlinePlayers().forEach(player ->
											player.sendActionBar(
													MistTextParser.parse(player, input)
											)
									);

									return Command.SINGLE_SUCCESS;
								})
						)
				)

				.build();
	}
}
