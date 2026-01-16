package com.xyrisdev.mist.redis.internal;

import com.xyrisdev.mist.redis.channel.RedisChannel;
import com.xyrisdev.mist.redis.codec.GsonCodec;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import org.jetbrains.annotations.NotNull;

public final class ChannelListener<T> extends RedisPubSubAdapter<String, String> {

    private final RedisChannel<T> channel;
    private final GsonCodec<T> codec;

    public ChannelListener(@NotNull RedisChannel<T> channel, @NotNull GsonCodec<T> codec) {
        this.channel = channel;
        this.codec = codec;
    }

    @Override
    public void message(@NotNull String channel, String message) {
        if (!channel.equals(this.channel.name())) {
            return;
        }

        this.channel.handle(this.codec.decode(message));
    }
}
