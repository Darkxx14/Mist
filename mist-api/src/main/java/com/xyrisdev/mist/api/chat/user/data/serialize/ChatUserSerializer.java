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

/**
 * Handles serialization and deserialization of ChatUser objects to and from JSON.
 *
 * <p>This class provides methods to convert ChatUser instances into JSON strings
 * for storage in databases, and to reconstruct ChatUser objects from JSON data.
 * The serialization includes all user data.</p>
 *
 * @since 1.0.0
 */
public final class ChatUserSerializer {

	private static final Gson GSON = new GsonBuilder()
										.setPrettyPrinting()
										.create();

	/**
	 * Serializes a ChatUser into a JSON string.
	 *
	 * <p>The resulting JSON contains all user data in a structured format
	 * suitable for storage in databases or file systems.</p>
	 *
	 * @param user the ChatUser to serialize
	 * @return JSON representation of the user
	 */
	public static @NotNull String serialize(@NotNull ChatUser user) {
		final ChatUserData data = new ChatUserData();

		data.settings = new EnumMap<>(ChatSettingType.class);
		data.ignored = new HashSet<>(user.ignore().snapshot());

		for (ChatSettingType type : ChatSettingType.values()) {
			data.settings.put(type, user.settings().enabled(type));
		}

		return GSON.toJson(data);
	}

	/**
	 * Deserializes a ChatUser from JSON data.
	 *
	 * <p>Reconstructs a ChatUser instance from its JSON representation,
	 * preserving all settings, ignore list entries, and message history.</p>
	 *
	 * @param id the UUID that should be assigned to the deserialized user
	 * @param json the JSON data to deserialize
	 * @return the reconstructed ChatUser
	 * @throws IllegalArgumentException if the JSON is malformed
	 */
	public static @NotNull ChatUser deserialize(@NotNull UUID id, @NotNull String json) {
		ChatUserData data = json.isBlank()
							? ChatUserData.defaults()
							: GSON.fromJson(json, ChatUserData.class);

		if (data == null) {
			data = ChatUserData.defaults();
		}

		final ChatUser user = new ChatUser(id);

		for (ChatSettingType type : ChatSettingType.values()) {
			if (!data.settings.getOrDefault(type, true)) {
				user.settings().disable(type);
			}
		}

		data.ignored.forEach(user.ignore()::add);
		return user;
	}
}
