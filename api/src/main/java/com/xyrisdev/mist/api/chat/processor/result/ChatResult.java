package com.xyrisdev.mist.api.chat.processor.result;

/**
 * Represents the result of chat message processing.
 *
 * <p>This enum determines whether chat processing should continue or be cancelled.</p>
 *
 * @since 1.0.0
 */
public enum ChatResult {

	/**
	 * Continue processing the chat message through remaining extensions.
	 */
	CONTINUE,

	/**
	 * Cancel the chat message and prevent it from being sent.
	 */
	CANCEL
}