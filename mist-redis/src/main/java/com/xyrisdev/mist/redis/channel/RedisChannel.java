package com.xyrisdev.mist.redis.channel;

import org.jetbrains.annotations.NotNull;

public interface RedisChannel<T> {

	@NotNull
	String name();

	void handle(@NotNull T payload);
}
