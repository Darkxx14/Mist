package com.xyrisdev.mist.adapter.redis.codec;

import io.lettuce.core.codec.RedisCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class StringByteArrayCodec implements RedisCodec<@NotNull String, byte @NotNull []> {

    @Override
    public @NotNull String decodeKey(@NotNull ByteBuffer buffer) {
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    @Override
    public byte @NotNull [] decodeValue(@NotNull ByteBuffer buffer) {
       final byte[] bytes = new byte[buffer.remaining()];

        buffer.get(bytes);

        return bytes;
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ByteBuffer encodeKey(@NotNull String key) {
        return StandardCharsets.UTF_8.encode(key);
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ByteBuffer encodeValue(byte @NotNull [] value) {
        return ByteBuffer.wrap(value);
    }
}
