package com.xyrisdev.mist.api.chat.user;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Manages loading, caching, and persistence of {@link ChatUser} instances.
 *
 * <p>This manager provides controlled access to chat users and tracks
 * modifications for asynchronous saving.</p>
 *
 * @since 1.0.0
 */
public interface ChatUserManager {

	/**
	 * Loads a chat user by unique identifier.
	 *
	 * @param uniqueId the user's UUID
	 * @return a future completing with the loaded user
	 */
	@NotNull CompletableFuture<ChatUser> load(@NotNull UUID uniqueId);

	/**
	 * Gets a cached chat user.
	 *
	 * @param uniqueId the user's UUID
	 * @return the cached user, or {@code null} if not loaded
	 */
	@Nullable ChatUser get(@NotNull UUID uniqueId);

	/**
	 * Gets a cached chat user for a player.
	 *
	 * @param player the player
	 * @return the cached user, or {@code null} if not loaded
	 */
	default @Nullable ChatUser get(@NotNull Player player) {
		return get(player.getUniqueId());
	}

	/**
	 * Applies a modification to a cached chat user.
	 *
	 * @param uniqueId the user's UUID
	 * @param action   the mutation action
	 */
	void modify(@NotNull UUID uniqueId, @NotNull Consumer<ChatUser> action);

	/**
	 * Applies a modification to a cached chat user for a player.
	 *
	 * @param player the player
	 * @param action the mutation action
	 */
	default void modify(@NotNull Player player, @NotNull Consumer<ChatUser> action) {
		modify(player.getUniqueId(), action);
	}

	/**
	 * Saves a modified chat user if necessary.
	 *
	 * @param uniqueId the user's UUID
	 */
	void save(@NotNull UUID uniqueId);

	/**
	 * Saves all modified cached users.
	 */
	void flush();

	/**
	 * Removes a chat user from the cache.
	 *
	 * @param uniqueId the user's UUID
	 */
	void invalidate(@NotNull UUID uniqueId);

	/**
	 * Removes all cached chat users.
	 */
	void invalidateAll();
}
