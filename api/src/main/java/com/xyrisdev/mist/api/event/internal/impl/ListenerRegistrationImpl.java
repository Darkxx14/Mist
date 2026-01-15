package com.xyrisdev.mist.api.event.internal.impl;

import com.xyrisdev.mist.api.event.internal.EventSubscription;
import com.xyrisdev.mist.api.event.internal.ListenerRegistration;
import com.xyrisdev.mist.api.event.MistEvent;
import com.xyrisdev.mist.api.event.internal.RegisteredListener;
import com.xyrisdev.mist.api.event.internal.registry.EventRegistry;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Default internal implementation of {@link ListenerRegistration}.
 *
 * <p>This class acts as a mutable builder during listener registration,
 * collecting configuration such as priority and cancellation behavior
 * before producing a {@link RegisteredListener}.</p>
 *
 * <p>Instances of this class are short-lived and exist only during
 * listener registration.</p>
 *
 * @param <T> the event type being registered
 */
public final class ListenerRegistrationImpl<T extends MistEvent> implements ListenerRegistration<T> {

    private final EventRegistry<T> registry;
    private final Class<T> eventClass;

    private EventPriority priority = EventPriority.NORMAL;
    private boolean ignoreCancelled;

    public ListenerRegistrationImpl(@NotNull EventRegistry<T> registry, @NotNull Class<T> eventClass) {
        this.registry = registry;
        this.eventClass = eventClass;
    }

    @Override
    public ListenerRegistration<T> priority(@NotNull EventPriority priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public ListenerRegistration<T> ignoreCancelled() {
        this.ignoreCancelled = true;
        return this;
    }

    @Override
    public @NotNull EventSubscription<T> run(final Consumer<? super T> handler) {
        final RegisteredListener<T> listener = new RegisteredListener<>(
            this.eventClass,
            handler,
            this.priority,
            this.ignoreCancelled,
            this.registry
        );

        this.registry.register(listener);
        return listener;
    }
}
