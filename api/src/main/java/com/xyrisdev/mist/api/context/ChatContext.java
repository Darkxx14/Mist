package com.xyrisdev.mist.api.context;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

public final class ChatContext {

	private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

	private final Player player;

	private Component message;
	private String plainMessage;

	private Object format;

	private boolean cancelled;

	public ChatContext(Player player, Component message) {
		this.player = player;
		this.message = message;
		this.plainMessage = PLAIN.serialize(message);
	}

	public Player player() {
		return player;
	}

	public Component message() {
		return message;
	}

	public void message(Component message) {
		this.message = message;
		this.plainMessage = PLAIN.serialize(message);
	}

	public String plain() {
		return plainMessage;
	}

	public void plain(String plain) {
		this.plainMessage = plain;
		this.message = Component.text(plain);
	}

	public <T> void format(T value) {
		this.format = value;
	}

	@SuppressWarnings("unchecked")
	public <T> T format(Class<T> type) {
		return (T) format;
	}

	public boolean cancelled() {
		return cancelled;
	}

	public void cancel() {
		this.cancelled = true;
	}
}
