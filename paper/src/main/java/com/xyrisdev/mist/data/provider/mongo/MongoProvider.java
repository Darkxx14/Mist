package com.xyrisdev.mist.data.provider.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;
import com.xyrisdev.mist.data.DatabaseProvider;
import com.xyrisdev.mist.data.provider.mongo.config.MongoConfig;
import com.xyrisdev.mist.data.user.mongo.MongoChatUserRepository;
import org.jetbrains.annotations.NotNull;

public class MongoProvider implements DatabaseProvider {

	private final MongoConfig config;

	private MongoClient client;
	private MongoDatabase database;
	private ChatUserRepository users;

	public MongoProvider(@NotNull MongoConfig config) {
		this.config = config;
	}

	@Override
	public void connect() {
		this.client = MongoClients.create(config.uri());
		this.database = client.getDatabase(config.database());

		this.users = new MongoChatUserRepository(
				database.getCollection(config.usersCollection())
		);
	}

	@Override
	public ChatUserRepository users() {
		return users;
	}

	@SuppressWarnings("unused")
	public MongoDatabase database() {
		return this.database;
	}

	@Override
	public void shutdown() {
		if (this.client != null) {
			this.client.close();
		}
	}
}
