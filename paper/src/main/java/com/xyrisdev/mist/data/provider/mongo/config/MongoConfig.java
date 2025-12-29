package com.xyrisdev.mist.data.provider.mongo.config;

import com.xyrisdev.library.config.CachableConfiguration;
import org.jetbrains.annotations.NotNull;

public class MongoConfig {

	private final String uri;
	private final String database;

	//collections
	private final String users;

	private MongoConfig(@NotNull String uri, @NotNull String database, @NotNull String users) {
		this.uri = uri;
		this.database = database;

		// collections
		this.users = users;
	}

	public static @NotNull MongoConfig from(@NotNull CachableConfiguration config) {
		return new MongoConfig(
				config.getString("data.mongo.uri", "mongodb://localhost:27017"),
				config.getString("data.mongo.database", "mist_data"),
				config.getString("data.mongo.collections.users", "mist_users")
		);
	}

	public String uri() {
		return this.uri;
	}

	public String database() {
		return this.database;
	}

	public String users() {
		return this.users;
	}
}
