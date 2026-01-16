package com.xyrisdev.mist.redis;

import com.xyrisdev.mist.redis.channel.RedisChannel;
import com.xyrisdev.mist.redis.codec.GsonCodec;
import com.xyrisdev.mist.redis.internal.ChannelListener;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.jetbrains.annotations.NotNull;

public record LettuceManager(
		@NotNull RedisClient client,
		@NotNull StatefulRedisConnection<String, String> commands,
		@NotNull StatefulRedisPubSubConnection<String, String> pubSub
) implements AutoCloseable {

	public static @NotNull LettuceManager of(@NotNull String uri) {
		final RedisClient client = RedisClient.create(uri);

		return new LettuceManager(
				client,
				client.connect(),
				client.connectPubSub()
		);
	}

	public <T> void register(@NotNull RedisChannel<T> channel, @NotNull GsonCodec<T> codec) {
		this.pubSub.addListener(new ChannelListener<>(channel, codec));
		this.pubSub.sync().subscribe(channel.name());
	}

	public <T> void publish(@NotNull RedisChannel<T> channel, @NotNull GsonCodec<T> codec, @NotNull T payload) {
		this.commands.async().publish(
				channel.name(),
				codec.encode(payload)
		);
	}

	@Override
	public void close() {
		this.pubSub.close();
		this.commands.close();
		this.client.shutdown();
	}
}
