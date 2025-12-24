package com.xyrisdev.mist.api.chat.user;

import com.xyrisdev.mist.api.chat.user.ignore.ChatIgnore;
import com.xyrisdev.mist.api.chat.user.toggle.ChatToggles;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChatUser {

	private final UUID id;
	private final ChatToggles toggles;
	private final ChatIgnore ignore;

	public ChatUser(@NotNull UUID uuid) {
		this.id = uuid;
		this.toggles = ChatToggles.defaults();
		this.ignore = new ChatIgnore();
	}

	public @NotNull UUID id() {
		return id;
	}

	public @NotNull ChatToggles toggles() {
		return toggles;
	}

	public @NotNull ChatIgnore ignore() {
		return ignore;
	}
}
