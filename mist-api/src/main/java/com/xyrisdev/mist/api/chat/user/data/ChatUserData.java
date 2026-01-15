package com.xyrisdev.mist.api.chat.user.data;

import com.xyrisdev.mist.api.chat.user.toggle.ChatSettingType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Simple data holder for user chat preferences and ignore list.
 *
 * <p>Used when saving/loading user data to/from storage.
 *
 * @since 1.0.0
 */
public final class ChatUserData {

	public Map<ChatSettingType, Boolean> settings;
	public Set<UUID> ignored;

	/**
	 * Creates new user data with all settings turned on and no ignored players.
	 *
	 * @return fresh ChatUserData with default values
	 */
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