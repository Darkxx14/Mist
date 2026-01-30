package com.xyrisdev.mist.messaging.chat.sync;

import com.xyrisdev.mist.api.messaging.MessagingBus;
import com.xyrisdev.mist.messaging.chat.codec.ChatPacketCodec;
import com.xyrisdev.mist.messaging.chat.packet.ChatPacket;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public final class ChatSyncService {

    private static final String channel = "mist.chat.global";

    private final MessagingBus bus;
    private final ChatPacketCodec codec;

    public ChatSyncService(@NotNull MessagingBus bus) {
        this.bus = bus;
        this.codec = new ChatPacketCodec();
    }

    public void publish(@NotNull String server, @NotNull UUID sender, @NotNull String message) {
        bus.publish(channel, codec.encode(server, sender, message));
    }

    public void subscribe(@NotNull Consumer<ChatPacket> consumer) {
        bus.subscribe(channel, data -> consumer.accept(codec.decode(data)));
    }
}
