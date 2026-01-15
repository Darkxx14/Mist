package com.xyrisdev.mist.api.event.user;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.event.MistEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Fired immediately before a {@link ChatUser} is modified.
 *
 * <p>The user instance has not yet been mutated when this
 * event is dispatched.</p>
 *
 * @since 1.0.0
 */
public final class ChatUserPreModifyEvent implements MistEvent {

    private final ChatUser user;

    public ChatUserPreModifyEvent(@NotNull ChatUser user) {
        this.user = user;
    }

    /**
     * Returns the chat user about to be modified.
     */
    public @NotNull ChatUser user() {
        return user;
    }
}
