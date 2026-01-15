package com.xyrisdev.mist.api.event.user;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.event.MistEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Fired after a {@link ChatUser} has been persisted to storage.
 *
 * <p>This event indicates that the user state has been successfully
 * written to the backing repository.</p>
 *
 * @since 1.0.0
 */
public final class ChatUserSaveEvent implements MistEvent {

    private final ChatUser user;

    public ChatUserSaveEvent(@NotNull ChatUser user) {
        this.user = user;
    }

    /**
     * Returns the saved chat user.
     */
    public @NotNull ChatUser user() {
        return user;
    }
}
