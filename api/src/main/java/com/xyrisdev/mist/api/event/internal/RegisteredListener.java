package com.xyrisdev.mist.api.event.internal;

import com.xyrisdev.mist.api.event.MistEvent;
import com.xyrisdev.mist.api.event.internal.registry.EventRegistry;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Internal representation of a registered event listener.
 *
 * <p>This class wraps the user-provided handler along with its execution
 * metadata, such as priority, cancellation behavior, and active state.</p>
 *
 * <p>Instances are managed exclusively by {@link EventRegistry} and should
 * not be created or manipulated directly by API consumers.</p>
 *
 * @param <T> the event type handled by this listener
 */
public class RegisteredListener<T extends MistEvent> implements EventSubscription<T> {

    private final Class<T> eventClass;
    private final Consumer<? super T> handler;
    private final EventPriority priority;
    private final boolean ignoreCancelled;
    private final EventRegistry<T> registry;

    private final AtomicBoolean active = new AtomicBoolean(true);

	public RegisteredListener(
       @NotNull Class<T> eventClass,
       @NotNull Consumer<? super T> handler,
       @NotNull EventPriority priority,
       boolean ignoreCancelled,
       @NotNull EventRegistry<T> registry
    ) {
        this.eventClass = eventClass;
        this.handler = handler;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
        this.registry = registry;
    }

	public EventPriority priority() {
        return this.priority;
    }

	public boolean ignoreCancelled() {
        return this.ignoreCancelled;
    }

    @Override
    public Class<T> eventClass() {
        return this.eventClass;
    }

    @Override
    public boolean active() {
        return this.active.get();
    }

    @Override
    public Consumer<? super T> handler() {
        return this.handler;
    }

    @Override
    public void close() {
        if (this.active.compareAndSet(true, false)) {
	        this.registry.unregister(this);
        }
    }
}
