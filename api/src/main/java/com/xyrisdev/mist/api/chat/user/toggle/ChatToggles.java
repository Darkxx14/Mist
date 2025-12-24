package com.xyrisdev.mist.api.chat.user.toggle;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class ChatToggles {

	private final EnumMap<ChatToggleType, Boolean> states;

	private ChatToggles(EnumMap<ChatToggleType, Boolean> states) {
		this.states = states;
	}

	public static @NotNull ChatToggles defaults() {
		final EnumMap<ChatToggleType, Boolean> map = new EnumMap<>(ChatToggleType.class);

		for (ChatToggleType type : ChatToggleType.values()) {
			map.put(type, true);
		}

		return new ChatToggles(map);
	}

	public boolean enabled(@NotNull ChatToggleType type) {
		return states.getOrDefault(type, true);
	}

	public void enable(@NotNull ChatToggleType type) {
		states.put(type, true);
	}

	public void disable(@NotNull ChatToggleType type) {
		states.put(type, false);
	}

	public boolean globalChat() {
		return enabled(ChatToggleType.GLOBAL_CHAT);
	}

	public boolean privateMessages() {
		return enabled(ChatToggleType.PRIVATE_MESSAGES);
	}

	public boolean mentions() {
		return enabled(ChatToggleType.MENTIONS);
	}

	public boolean announcements() {
		return enabled(ChatToggleType.ANNOUNCEMENTS);
	}
}
