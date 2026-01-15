package com.xyrisdev.mist.api.chat.user.repository;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Repository interface for persisting and retrieving ChatUser data.
 *
 * <p>This interface defines the contract for user data storage, allowing
 * different implementations (H2, MySQL, MongoDB, etc.) to be used interchangeably.</p>
 *
 * @since 1.0.0
 */
public interface ChatUserRepository {

	/**
	 * Loads a ChatUser from persistent storage.
	 *
	 * <p>If the user does not exist in storage, a new ChatUser with default
	 * settings should be created and returned.</p>
	 *
	 * @param id the UUID of the user to load
	 * @return the loaded or created ChatUser
	 */
	@NotNull ChatUser load(@NotNull UUID id);

	/**
	 * Saves a ChatUser to persistent storage.
	 *
	 * @param user the ChatUser to save
	 */
	void save(@NotNull ChatUser user);

	/**
	 * Deletes a ChatUser from persistent storage.
	 *
	 * @param id the UUID of the user to delete
	 */
	void delete(@NotNull UUID id);
}