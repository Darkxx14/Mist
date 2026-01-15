package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.Bukkit;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class ChatCommand {

	private static final int CLEAR_LINES = 150;
	private static final AtomicBoolean LOCKED = new AtomicBoolean(false);

	public static boolean getLocked() {
		return LOCKED.get();
	}

	@Command("mist chat clear")
	@Permission("mist.command.chat")
	public void clear() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			IntStream.range(0, CLEAR_LINES)
					.forEach(i -> player.sendMessage(" "));
			MistMessage.create(player).id("chat_cleared").send();
		});
	}

	@Command("mist chat lock")
	@Permission("mist.command.chat")
	public void lock(Source sender) {
		if (!LOCKED.compareAndSet(false, true)) {
			MistMessage.create(sender.source())
					.id("chat_already_locked")
					.send();
			return;
		}

		Bukkit.getOnlinePlayers().forEach(player ->
				MistMessage.create(player).id("chat_locked").send()
		);
	}

	@Command("mist chat unlock")
	@Permission("mist.command.chat")
	public void unlock(Source sender) {
		if (!LOCKED.compareAndSet(true, false)) {
			MistMessage.create(sender.source())
					.id("chat_already_unlocked")
					.send();
			return;
		}

		Bukkit.getOnlinePlayers().forEach(player ->
				MistMessage.create(player).id("chat_unlocked").send()
		);
	}
}
