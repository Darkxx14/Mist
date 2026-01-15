package com.xyrisdev.mist.api.event.internal;

import com.xyrisdev.mist.api.event.MistEvent;

import java.util.function.Consumer;

/**
 * Internal representation of a registered event handler.
 *
 * <p>This type is used internally by the Mist event system to track
 * listener state and lifecycle.</p>
 *
 * <p>This interface is not part of the public API and may change
 * without notice.</p>
 *
 * @param <T> the event type
 *
 * @since 1.0.0
 */
public interface EventSubscription<T extends MistEvent> extends AutoCloseable {

    Class<T> eventClass();

    boolean active();

	Consumer<T> handler();

    @Override
    void close();
}
