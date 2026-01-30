package com.xyrisdev.mist.adapter.nats;

import com.xyrisdev.mist.api.messaging.MessagingBus;
import com.xyrisdev.mist.api.messaging.MessagingSubscriber;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class NATSMessagingBus implements MessagingBus {

    private final Connection connection;

    public NATSMessagingBus(@NotNull String uri) {
        try {
            this.connection = Nats.connect(
                    new Options.Builder()
                            .server(uri)
                            .connectionTimeout(Duration.ofSeconds(5))
                            .maxReconnects(0)
                            .build()
            );
        } catch (Exception e) {
            throw new IllegalStateException("failed to connect to nats", e);
        }
    }

    @Override
    public void publish(@NotNull String channel, byte @NotNull [] payload) {
        connection.publish(channel, payload);
    }

    @Override
    public void subscribe(@NotNull String channel, @NotNull MessagingSubscriber subscriber) {
        final Dispatcher dispatcher = connection.createDispatcher(msg -> {
            if (channel.equals(msg.getSubject())) {
                subscriber.onMessage(msg.getData());
            }
        });

        dispatcher.subscribe(channel);
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
