package com.xyrisdev.mist.redis.packet;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ChatPacket(
        @NotNull UUID sender,
        @NotNull String server,
        @NotNull String message
) {}
