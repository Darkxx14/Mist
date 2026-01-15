package com.xyrisdev.mist.api.chat.user;

import com.xyrisdev.mist.api.chat.user.ignore.ChatIgnore;
import com.xyrisdev.mist.api.chat.user.toggle.ChatSettings;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.UUID;

/**
 * Represents a chat user in Mist.
 *
 * <p>Each ChatUser contains their unique identifier, chat settings, and ignore list.
 * This class serves as the primary data model for user-specific chat functionality.</p>
 *
 * @since 1.0.0
 */
public final class ChatUser {

	private final UUID id;
	private final ChatSettings settings;
	private final ChatIgnore ignore;

	/**
	 * Creates a new ChatUser with the specified UUID.
	 *
	 * <p>The user will be initialized with default chat settings and an empty ignore list.</p>
	 *
	 * @param id the unique identifier for this user
	 */
	public ChatUser(@NotNull UUID id) {
		this.id = id;
		this.settings = ChatSettings.defaults();
		this.ignore = new ChatIgnore();
	}

	/**
	 * Creates a snapshot copy of another {@link ChatUser}.
	 * @param other the user to copy
	 */
	public ChatUser(@NotNull ChatUser other) {
		this.id = other.id;
		this.settings = new ChatSettings(new EnumMap<>(other.settings().states()));
		this.ignore = new ChatIgnore();
		other.ignore().snapshot().forEach(this.ignore::add);
	}

	/**
	 * Gets the unique identifier of this chat user.
	 *
	 * @return the user's UUID
	 */
	public @NotNull UUID id() {
		return this.id;
	}

	/**
	 * Gets the chat settings for this user.
	 *
	 * <p>Settings control various chat features like global chat visibility,
	 * private messages, mentions, and announcements.</p>
	 *
	 * @return the user's chat settings
	 */
	public @NotNull ChatSettings settings() {
		return this.settings;
	}

	/**
	 * Gets the ignore list manager for this user.
	 *
	 * <p>The ignore list controls which other players this user has chosen to ignore.</p>
	 *
	 * @return the user's ignore list manager
	 */
	public @NotNull ChatIgnore ignore() {
		return this.ignore;
	}
}
