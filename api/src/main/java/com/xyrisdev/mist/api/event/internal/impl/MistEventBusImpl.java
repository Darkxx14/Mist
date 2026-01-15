package com.xyrisdev.mist.api.event.internal.impl;

import com.xyrisdev.mist.api.event.internal.ListenerRegistration;
import com.xyrisdev.mist.api.event.MistEvent;
import com.xyrisdev.mist.api.event.MistEventBus;
import com.xyrisdev.mist.api.event.internal.registry.EventRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default internal implementation of {@link MistEventBus}.
 *
 * <p>This class is responsible for managing event registries and dispatching
 * events to registered listeners.</p>
 *
 * <p>Events are dispatched in the same execution context that
 * invokes {@link #post(MistEvent)}. No thread switching or scheduling is
 * performed.</p>
 *
 * <p>This class is considered an internal implementation detail and is not
 * intended for direct use outside the Mist runtime.</p>
 */
public class MistEventBusImpl implements MistEventBus {

	private final Map<Class<?>, EventRegistry<?>> registries = new ConcurrentHashMap<>();

	@Override
	public <T extends MistEvent> @NotNull ListenerRegistration<T> on(@NotNull Class<T> eventClass) {
		return new ListenerRegistrationImpl<>(registry(eventClass), eventClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void post(final @NotNull MistEvent event) {
		final EventRegistry<MistEvent> registry = (EventRegistry<MistEvent>) this.registries.get(event.getClass());

		if (registry != null) {
			registry.dispatch(event);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends MistEvent> EventRegistry<T> registry(final Class<T> type) {
		return (EventRegistry<T>) this.registries.computeIfAbsent(type, ignored -> new EventRegistry<>());
	}
}
