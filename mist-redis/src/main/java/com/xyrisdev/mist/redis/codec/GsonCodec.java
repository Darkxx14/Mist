package com.xyrisdev.mist.redis.codec;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

public record GsonCodec<T>(
		@NotNull Gson gson,
		@NotNull Class<T> type
) {

	public String encode(@NotNull T value) {
		return this.gson.toJson(value);
	}

	public T decode(@NotNull String payload) {
		return this.gson.fromJson(payload, this.type);
	}
}
