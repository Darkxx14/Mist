package com.xyrisdev.mist.api.event.internal;

import com.xyrisdev.mist.api.event.MistEvent;
import com.xyrisdev.mist.api.event.MistEventBus;
import org.bukkit.event.EventPriority;

import java.util.function.Consumer;

/**
 * Internal fluent builder used during event listener registration.
 *
 * <p>This type exists solely to support the {@link MistEventBus#on(Class)}
 * registration flow and is not intended for direct use by API consumers.</p>
 *
 * @param <T> the event type
 */
public interface ListenerRegistration<T extends MistEvent> {

    ListenerRegistration<T> priority(EventPriority priority);

    ListenerRegistration<T> ignoreCancelled();

    EventSubscription<T> run(Consumer<T> handler);
}
