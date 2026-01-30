package com.xyrisdev.mist.adapter.redis;

import com.xyrisdev.mist.adapter.redis.codec.StringByteArrayCodec;
import com.xyrisdev.mist.api.messaging.MessagingBus;
import com.xyrisdev.mist.api.messaging.MessagingSubscriber;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.jetbrains.annotations.NotNull;

public final class RedisMessagingBus implements MessagingBus {

	private final RedisClient client;
	private final StatefulRedisConnection<String, byte[]> cmds;
	private final StatefulRedisPubSubConnection<String, byte[]> pubSub;

	public RedisMessagingBus(@NotNull String uri) {
		this.client = RedisClient.create(uri);

		final StringByteArrayCodec codec = new StringByteArrayCodec();

		this.cmds = client.connect(codec);
		this.pubSub = client.connectPubSub(codec);
	}

	@Override
	public void publish(@NotNull String channel, byte @NotNull [] payload) {
		cmds.async().publish(channel, payload);
	}

	@Override
	public void subscribe(@NotNull String channel, @NotNull MessagingSubscriber subscriber) {
		pubSub.addListener(new RedisPubSubAdapter<>() {
			@Override
			public void message(@NotNull String ch, byte @NotNull [] message) {
				if (ch.equals(channel)) {
					subscriber.onMessage(message);
				}
			}
		});

		pubSub.sync().subscribe(channel);
	}

	@Override
	public void close() {
		pubSub.close();
		cmds.close();
		client.shutdown();
	}
}
