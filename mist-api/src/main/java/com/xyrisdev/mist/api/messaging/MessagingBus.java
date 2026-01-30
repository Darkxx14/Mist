package com.xyrisdev.mist.api.messaging;

import org.jetbrains.annotations.NotNull;

public interface MessagingBus extends AutoCloseable {

    void publish(@NotNull String channel, byte @NotNull [] payload);

    void subscribe(@NotNull String channel, @NotNull MessagingSubscriber subscriber);

    @Override
    void close();

}
