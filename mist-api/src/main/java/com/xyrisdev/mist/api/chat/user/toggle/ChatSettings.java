package com.xyrisdev.mist.api.chat.user.toggle;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

/**
 * Manages chat settings for a user, providing toggle functionality for various chat features.
 *
 * <p>Each setting can be independently enabled or disabled,
 * allowing users to customize their chat experience.</p>
 *
 * <p>Settings are persisted to the database and affect how users receive and interact
 * with chat messages, including global chat visibility, private messages, mentions,
 * and server announcements.</p>
 *
 * @param states an EnumMap containing the enabled/disabled state for each ChatSettingType
 * @since 1.0.0
 */
public record ChatSettings(@NotNull EnumMap<ChatSettingType, Boolean> states) {

	/**
	 * Creates a new ChatSettings instance with all settings enabled by default.
	 *
	 * <p>This factory method initializes the settings map with all ChatSettingType
	 * values set to true, representing the default enabled state for new users.</p>
	 *
	 * @return a new ChatSettings with all settings enabled
	 */
	public static @NotNull ChatSettings defaults() {
		final EnumMap<ChatSettingType, Boolean> map = new EnumMap<>(ChatSettingType.class);

		for (ChatSettingType type : ChatSettingType.values()) {
			map.put(type, true);
		}

		return new ChatSettings(map);
	}

	/**
	 * Checks if a specific chat setting is enabled for this user.
	 *
	 * <p>If the setting type is not present in the states map, this method
	 * returns true as a default value, treating unknown settings as enabled.</p>
	 *
	 * @param type the setting type to check
	 * @return true if the setting is enabled, false if disabled
	 */
	public boolean enabled(@NotNull ChatSettingType type) {
		return this.states.getOrDefault(type, true);
	}

	/**
	 * Enables a specific chat setting for this user.
	 *
	 * <p>This method updates the internal states map to mark the setting as enabled.
	 * The change will be persisted when the user data is saved.</p>
	 *
	 * @param type the setting type to enable
	 */
	public void enable(@NotNull ChatSettingType type) {
		this.states.put(type, true);
	}

	/**
	 * Disables a specific chat setting for this user.
	 *
	 * <p>This method updates the internal states map to mark the setting as disabled.
	 * The change will be persisted when the user data is saved.</p>
	 *
	 * @param type the setting type to disable
	 */
	public void disable(@NotNull ChatSettingType type) {
		this.states.put(type, false);
	}

	/**
	 * Checks if the user has global chat enabled.
	 *
	 * <p>When disabled, the user will not receive messages from the global chat channel.</p>
	 *
	 * @return true if global chat is enabled, false otherwise
	 */
	public boolean globalChat() {
		return enabled(ChatSettingType.GLOBAL_CHAT);
	}

	/**
	 * Checks if the user can receive private messages.
	 *
	 * <p>When disabled, incoming private messages from other players will be blocked.</p>
	 *
	 * @return true if private messages are enabled, false otherwise
	 */
	public boolean privateMessages() {
		return enabled(ChatSettingType.PRIVATE_MESSAGES);
	}

	/**
	 * Checks if the user receives mention notifications.
	 *
	 * <p>When disabled, the user will not be highlighted or notified when mentioned in chat.</p>
	 *
	 * @return true if mentions are enabled, false otherwise
	 */
	public boolean mentions() {
		return enabled(ChatSettingType.MENTIONS);
	}

	/**
	 * Checks if the user receives server announcements.
	 *
	 * <p>When disabled, automated server announcements and broadcasts will not be sent to this user.</p>
	 *
	 * @return true if announcements are enabled, false otherwise
	 */
	public boolean announcements() {
		return enabled(ChatSettingType.ANNOUNCEMENTS);
	}
}