package com.xyrisdev.mist.api.chat.user.toggle;

/**
 * Represents chat settings that users can toggle on or off.
 *
 * <p>Each setting represents a specific chat feature that can be enabled or disabled
 * by individual users. These settings are persisted and affect how users receive
 * and interact with chat messages.</p>
 *
 * @since 1.0.0
 */
public enum ChatSettingType {

	/**
	 * Controls whether the user receives messages from global chat.
	 * When disabled, the user won't see chat messages from other players.
	 */
	GLOBAL_CHAT,

	/**
	 * Controls whether the user can receive private messages from other players.
	 * When disabled, incoming private messages will be blocked.
	 */
	PRIVATE_MESSAGES,

	/**
	 * Controls whether the user receives notifications when mentioned in chat.
	 * When disabled, mention notifications and highlights will not be shown.
	 */
	MENTIONS,

	/**
	 * Controls whether the user receives automated server announcements.
	 * When disabled, scheduled announcements will not be sent to this user.
	 */
	ANNOUNCEMENTS
}
