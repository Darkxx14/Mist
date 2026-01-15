package com.xyrisdev.mist.api.event;

import com.xyrisdev.mist.api.event.internal.ListenerRegistration;

/**
 * Central event bus for Mist.
 *
 * <p>Events are dispatched in the same execution context that invokes
 * {@link #post(MistEvent)}. The event bus does not perform any thread
 * switching, scheduling, or context management.</p>
 */
public interface MistEventBus {

	/**
	 * Begins registration of a new listener.
	 *
	 * @param eventClass the event class
	 * @param <T>        the event type
	 * @return a listener registration builder
	 */
	<T extends MistEvent> ListenerRegistration<T> on(Class<T> eventClass);

	/**
	 * Dispatches an event to all registered listeners.
	 *
	 * <p>Listeners are invoked in priority order and execute
	 * on the caller's execution context.</p>
	 *
	 * @param event the event
	 */
	void post(MistEvent event);
}
