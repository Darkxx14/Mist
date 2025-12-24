package com.xyrisdev.mist.api.chat.context;

import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ChatContext {

	private final Player sender;
	private Component message;

	private final Map<Key, Object> attributes = new ConcurrentHashMap<>();
	private ChatResult result = ChatResult.CONTINUE;

	public ChatContext(Player sender, Component message) {
		this.sender = sender;
		this.message = message;
	}

	public Player sender() {
		return sender;
	}

	public Component message() {
		return message;
	}

	public void message(Component message) {
		this.message = message;
	}

	public <T> void attribute(Key key, T value) {
		attributes.put(key, value);
	}

	public <T> T attributes(Key key, @NotNull Class<T> type) {
		return type.cast(attributes.get(key));
	}

	public boolean has(Key key) {
		return attributes.containsKey(key);
	}

	public void cancel() {
		this.result = ChatResult.CANCEL;
	}

	public ChatResult result() {
		return result;
	}
}
