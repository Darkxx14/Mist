package com.xyrisdev.mist.data.provider.mongo.config;

import com.xyrisdev.library.config.CachableConfiguration;
import org.jetbrains.annotations.NotNull;

public class MongoConfig {

	private final String uri;
	private final String database;
	private final String usersCollection;

	private MongoConfig(String uri, String database, String usersCollection) {
		this.uri = uri;
		this.database = database;
		this.usersCollection = usersCollection;
	}

	public static @NotNull MongoConfig from(@NotNull CachableConfiguration config) {
		return new MongoConfig(
				config.getString("data.mongo.uri", "mongodb://localhost:27017"),
				config.getString("data.mongo.database", "mist_data"),
				config.getString("data.mongo.collections.users", "mist_users")
		);
	}

	public String uri() {
		return uri;
	}

	public String database() {
		return database;
	}

	public String usersCollection() {
		return usersCollection;
	}
}
