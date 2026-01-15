package com.xyrisdev.mist.api.chat.context;

import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the context of a chat message being processed through the Mist chat processor.
 *
 * <p>This class encapsulates all data related to a chat message, including the sender,
 * the message content (both as Component and plain text), and any additional attributes
 * added by extensions during processing.</p>
 *
 * <p>Extensions can store custom data using the {@link #attribute(Key, Object)} method
 * and retrieve it later with {@link #attributes(Key, Class)}. The context also tracks
 * the processing result through {@link #result()} and allows cancellation via {@link #cancel()}.</p>
 *
 * @since 1.0.0
 */
public final class ChatContext {

	private final Player sender;
	private Component message;
	private String plainMessage;

	private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
	private final Map<Key, Object> attributes = new ConcurrentHashMap<>();
	private ChatResult result = ChatResult.CONTINUE;

	/**
	 * Creates a new ChatContext for the given sender and message.
	 *
	 * @param sender the player who sent the message
	 * @param message the message content as an Adventure Component
	 */
	public ChatContext(Player sender, Component message) {
		this.sender = sender;
		this.message = message;
		this.plainMessage = PLAIN.serialize(message);
	}

	/**
	 * Gets the player who sent this chat message.
	 *
	 * @return the message sender
	 */
	public Player sender() {
		return this.sender;
	}

	/**
	 * Gets the current message content as an Adventure Component.
	 *
	 * <p>This may have been modified by extensions during processing.</p>
	 *
	 * @return the current message component
	 */
	public Component message() {
		return this.message;
	}

	/**
	 * Gets the plain text representation of the current message.
	 *
	 * @return the message as plain text
	 */
	public String plain() {
		return this.plainMessage;
	}

	/**
	 * Updates both the plain text and Component representation of the message.
	 *
	 * <p>This method is typically used by extensions that need to modify the message content.</p>
	 *
	 * @param plain the new plain text message
	 */
	public void plain(String plain) {
		this.plainMessage = plain;
		this.message = Component.text(plain);
	}

	/**
	 * Updates the message content as an Adventure Component.
	 *
	 * <p>The plain text representation will be automatically updated to match.</p>
	 *
	 * @param message the new message component
	 */
	public void message(Component message) {
		this.message = message;
		this.plainMessage = PLAIN.serialize(message);
	}

	/**
	 * Stores an attribute in this context for later retrieval by other extensions.
	 *
	 * <p>Attributes are stored using a Key to avoid conflicts between different extensions.</p>
	 *
	 * @param <T> the type of the value
	 * @param key the unique key for this attribute
	 * @param value the value to store
	 */
	public <T> void attribute(Key key, T value) {
		this.attributes.put(key, value);
	}

	/**
	 * Retrieves an attribute from this context.
	 *
	 * @param <T> the expected type of the value
	 * @param key the key of the attribute to retrieve
	 * @param type the expected type class for casting
	 * @return the stored value, or null if not found
	 * @throws ClassCastException if the stored value cannot be cast to the requested type
	 */
	public <T> T attributes(Key key, @NotNull Class<T> type) {
		return type.cast(this.attributes.get(key));
	}

	/**
	 * Cancels the chat message processing.
	 *
	 * <p>When called, the message will not be sent to chat and processing will stop.</p>
	 */
	public void cancel() {
		this.result = ChatResult.CANCEL;
	}

	/**
	 * Gets the current result of chat processing.
	 *
	 * @return the processing result
	 */
	public ChatResult result() {
		return this.result;
	}
}
