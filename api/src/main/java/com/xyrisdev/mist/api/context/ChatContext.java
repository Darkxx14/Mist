package com.xyrisdev.mist.api.context;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ChatContext {

	private final Player player;
	private Component message;
	private boolean cancelled;

	public ChatContext(@NotNull Player player, @NotNull Component message) {
		this.player = player;
		this.message = message;
	}

	public Player player() {
		return player;
	}

	public Component message() {
		return message;
	}

	public void message(Component message) {
		this.message = message;
	}

	public boolean cancelled() {
		return cancelled;
	}

	public void cancel() {
		this.cancelled = true;
	}
}
