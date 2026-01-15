package com.xyrisdev.mist.api.event.internal.registry;

import com.xyrisdev.mist.api.event.CancellableMistEvent;
import com.xyrisdev.mist.api.event.MistEvent;
import com.xyrisdev.mist.api.event.internal.RegisteredListener;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Internal registry responsible for storing and dispatching listeners
 * for a single event type.
 *
 * <p>Listeners are grouped by {@link EventPriority} and are executed in
 * priority order when an event is dispatched.</p>
 *
 * <p>This class performs no synchronization beyond what is required for
 * safe concurrent registration and unregistration.</p>
 *
 * @param <T> the event type handled by this registry
 */
public class EventRegistry<T extends MistEvent> {

    private final Map<EventPriority, List<RegisteredListener<T>>> listeners = new EnumMap<>(EventPriority.class);

    public EventRegistry() {
	    Arrays.stream(EventPriority.values()).forEach(p -> this.listeners.put(p, new CopyOnWriteArrayList<>()));
    }

	public void register(@NotNull RegisteredListener<T> listener) {
		this.listeners.get(listener.priority()).add(listener);
    }

	public void unregister(@NotNull RegisteredListener<T> listener) {
		this.listeners.get(listener.priority()).remove(listener);
    }

	public void dispatch(final T event) {
		for (EventPriority priority : EventPriority.values()) {
			for (RegisteredListener<T> listener : this.listeners.get(priority)) {

				if (!listener.active()) {
					continue;
				}

				if (listener.ignoreCancelled() && event instanceof CancellableMistEvent cancellable && cancellable.cancelled()) {
					continue;
				}

				listener.handler().accept(event);
			}
		}
	}
}
