package com.xyrisdev.mist.api.chat.user.ignore;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the ignore list for a chat user.
 *
 * <p>This class handles which players a user has chosen to ignore and provides
 * simple operations for adding, removing, and checking ignored players.</p>
 *
 * @since 1.0.0
 */
public final class ChatIgnore {

	private final Set<UUID> ignored = ConcurrentHashMap.newKeySet();

	/**
	 * Checks if the specified player UUID is in this ignore list.
	 *
	 * @param id the UUID of the player to check
	 * @return true if the player is ignored, false otherwise
	 */
	public boolean contains(@NotNull UUID id) {
		return this.ignored.contains(id);
	}

	/**
	 * Adds a player to this ignore list.
	 *
	 * <p>If the player is already ignored, this method has no effect.</p>
	 *
	 * @param id the UUID of the player to ignore
	 */
	public void add(@NotNull UUID id) {
		this.ignored.add(id);
	}

	/**
	 * Removes a player from this ignore list.
	 *
	 * <p>If the player is not in the ignore list, this method has no effect.</p>
	 *
	 * @param id the UUID of the player to unignore
	 */
	public void remove(@NotNull UUID id) {
		this.ignored.remove(id);
	}

	/**
	 * Creates an immutable snapshot of the current ignore list.
	 *
	 * <p>The returned set is a copy and will not reflect subsequent changes
	 * to this ignore list.</p>
	 *
	 * @return an immutable set containing all ignored player UUIDs
	 */
	@Contract(pure = true)
	public @NotNull @Unmodifiable Set<UUID> snapshot() {
		return Set.copyOf(this.ignored);
	}
}
