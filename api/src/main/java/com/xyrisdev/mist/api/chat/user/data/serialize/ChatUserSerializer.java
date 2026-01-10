package com.xyrisdev.mist.api.chat.user.data.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.data.ChatUserData;
import com.xyrisdev.mist.api.chat.user.toggle.ChatSettingType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.UUID;

public final class ChatUserSerializer {

	private static final Gson GSON = new GsonBuilder()
										.setPrettyPrinting()
										.create();

	public static @NotNull String serialize(@NotNull ChatUser user) {
		final ChatUserData data = new ChatUserData();

		data.settings = new EnumMap<>(ChatSettingType.class);
		data.ignored = new HashSet<>(user.ignore().snapshot());

		for (ChatSettingType type : ChatSettingType.values()) {
			data.settings.put(type, user.settings().enabled(type));
		}

		return GSON.toJson(data);
	}

	public static @NotNull ChatUser deserialize(@NotNull UUID uuid, @NotNull String raw) {
		ChatUserData data = raw.isBlank()
							? ChatUserData.defaults()
							: GSON.fromJson(raw, ChatUserData.class);

		if (data == null) {
			data = ChatUserData.defaults();
		}

		final ChatUser user = new ChatUser(uuid);

		for (ChatSettingType type : ChatSettingType.values()) {
			if (!data.settings.getOrDefault(type, true)) {
				user.settings().disable(type);
			}
		}

		data.ignored.forEach(user.ignore()::add);
		return user;
	}
}
