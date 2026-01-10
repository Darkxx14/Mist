package com.xyrisdev.mist.api.chat.user;

import com.xyrisdev.mist.api.chat.user.ignore.ChatIgnore;
import com.xyrisdev.mist.api.chat.user.toggle.ChatSettings;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChatUser {

	private final UUID id;
	private final ChatSettings settings;
	private final ChatIgnore ignore;

	public ChatUser(@NotNull UUID id) {
		this.id = id;
		this.settings = ChatSettings.defaults();
		this.ignore = new ChatIgnore();
	}

	public @NotNull UUID id() {
		return this.id;
	}

	public @NotNull ChatSettings settings() {
		return this.settings;
	}

	public @NotNull ChatIgnore ignore() {
		return this.ignore;
	}
}
