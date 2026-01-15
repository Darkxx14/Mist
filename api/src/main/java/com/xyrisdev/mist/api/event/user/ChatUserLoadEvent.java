package com.xyrisdev.mist.api.event.user;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.event.MistEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Fired after a {@link ChatUser} has been loaded from storage
 * and placed into the cache.
 *
 * <p>This event is only fired when the user is loaded from the
 * repository, not when returned from cache.</p>
 */
public final class ChatUserLoadEvent implements MistEvent {

	private final ChatUser user;

	public ChatUserLoadEvent(@NotNull ChatUser user) {
		this.user = user;
	}

	/**
	 * Returns the loaded chat user.
	 */
	public @NotNull ChatUser user() {
		return user;
	}
}
