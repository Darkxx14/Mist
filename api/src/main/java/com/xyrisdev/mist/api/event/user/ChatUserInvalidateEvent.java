package com.xyrisdev.mist.api.event.user;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.event.MistEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a {@link ChatUser} is invalidated from the cache.
 *
 * <p>After this event is dispatched, the user is no longer
 * present in memory.</p>
 */
public final class ChatUserInvalidateEvent implements MistEvent {

    private final ChatUser user;

    public ChatUserInvalidateEvent(@NotNull ChatUser user) {
        this.user = user;
    }

    /**
     * Returns the invalidated chat user.
     */
    public @NotNull ChatUser user() {
        return user;
    }
}
