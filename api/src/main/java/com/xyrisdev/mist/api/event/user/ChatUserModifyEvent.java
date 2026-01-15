package com.xyrisdev.mist.api.event.user;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.event.MistEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Fired after a {@link ChatUser} has been modified.
 *
 * <p>This event provides access to both the state before and
 * after modification.</p>
 */
public final class ChatUserModifyEvent implements MistEvent {

    private final ChatUser before;
    private final ChatUser after;

    public ChatUserModifyEvent(@NotNull ChatUser before, @NotNull ChatUser after) {
        this.before = before;
        this.after = after;
    }

    /**
     * Returns the user state before modification.
     */
    public @NotNull ChatUser before() {
        return before;
    }

    /**
     * Returns the user state after modification.
     */
    public @NotNull ChatUser after() {
        return after;
    }
}
