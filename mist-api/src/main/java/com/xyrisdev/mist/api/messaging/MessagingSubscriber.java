package com.xyrisdev.mist.api.messaging;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MessagingSubscriber {

    void onMessage(byte @NotNull [] payload);

}
