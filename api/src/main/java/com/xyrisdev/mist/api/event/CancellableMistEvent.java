package com.xyrisdev.mist.api.event;

/**
 * Represents a {@link MistEvent} that can be cancelled.
 * <p>
 * Cancelling an event signals that its default behavior should not be executed.
 */
public interface CancellableMistEvent extends MistEvent {

	/**
	 * Checks whether this event is currently cancelled.
	 *
	 * @return {@code true} if the event is cancelled, {@code false} otherwise
	 */
	boolean cancelled();

	/**
	 * Sets the cancelled state of this event.
	 *
	 * @param cancelled {@code true} to cancel the event, {@code false} to allow it
	 */
	void cancelled(boolean cancelled);
}
