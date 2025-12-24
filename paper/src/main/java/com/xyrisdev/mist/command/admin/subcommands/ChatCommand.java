package com.xyrisdev.mist.command.admin.subcommands;

import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.Bukkit;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class ChatCommand {

	private static final int CLEAR_LINES = 150;
	private static final AtomicBoolean LOCKED = new AtomicBoolean(false);

	public static boolean getLocked() {
		return LOCKED.get();
	}

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(root.literal("chat")
				.permission("mist.commands.chat")
				.literal("clear")
				.handler(this::clear)
		);

		manager.command(root.literal("chat")
				.permission("mist.commands.chat")
				.literal("lock")
				.handler(this::lock)
		);

		manager.command(root.literal("chat")
				.permission("mist.commands.chat")
				.literal("unlock")
				.handler(this::unlock)
		);
	}

	private void clear(@NotNull CommandContext<Source> ignored) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			IntStream.range(0, CLEAR_LINES)
					.mapToObj(i -> " ")
					.forEach(player::sendMessage);

			MistMessage.create(player)
					.id("chat_cleared")
					.send();
		});
	}

	private void lock(@NotNull CommandContext<Source> ctx) {
		if (!LOCKED.compareAndSet(false, true)) {
			MistMessage.create(ctx.sender().source())
					.id("chat_already_locked")
					.send();
			return;
		}

		Bukkit.getOnlinePlayers().forEach(player ->
				MistMessage.create(player)
						.id("chat_locked")
						.send()
		);
	}

	private void unlock(@NotNull CommandContext<Source> ctx) {
		if (!LOCKED.compareAndSet(true, false)) {
			MistMessage.create(ctx.sender().source())
					.id("chat_already_unlocked")
					.send();
			return;
		}

		Bukkit.getOnlinePlayers().forEach(player ->
				MistMessage.create(player)
						.id("chat_unlocked")
						.send()
		);
	}
}
