package com.xyrisdev.mist.api.chat.user.toggle;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class ChatSettings {

	private final EnumMap<ChatSettingType, Boolean> states;

	private ChatSettings(@NotNull EnumMap<ChatSettingType, Boolean> states) {
		this.states = states;
	}

	public static @NotNull ChatSettings defaults() {
		final EnumMap<ChatSettingType, Boolean> map = new EnumMap<>(ChatSettingType.class);

		for (ChatSettingType type : ChatSettingType.values()) {
			map.put(type, true);
		}

		return new ChatSettings(map);
	}

	public boolean enabled(@NotNull ChatSettingType type) {
		return this.states.getOrDefault(type, true);
	}

	public void enable(@NotNull ChatSettingType type) {
		this.states.put(type, true);
	}

	public void disable(@NotNull ChatSettingType type) {
		this.states.put(type, false);
	}

	public boolean globalChat() {
		return enabled(ChatSettingType.GLOBAL_CHAT);
	}

	public boolean privateMessages() {
		return enabled(ChatSettingType.PRIVATE_MESSAGES);
	}

	public boolean mentions() {
		return enabled(ChatSettingType.MENTIONS);
	}

	public boolean announcements() {
		return enabled(ChatSettingType.ANNOUNCEMENTS);
	}
}
