package com.xyrisdev.mist.api.chat.user.data;

import com.xyrisdev.mist.api.chat.user.toggle.ChatSettingType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ChatUserData {

	public EnumMap<ChatSettingType, Boolean> settings;
	public Set<UUID> ignored;

	public static @NotNull ChatUserData defaults() {
		final ChatUserData data = new ChatUserData();

		data.settings = new EnumMap<>(ChatSettingType.class);
		data.ignored = new HashSet<>();

		for (ChatSettingType type : ChatSettingType.values()) {
			data.settings.put(type, true);
		}

		return data;
	}
}
