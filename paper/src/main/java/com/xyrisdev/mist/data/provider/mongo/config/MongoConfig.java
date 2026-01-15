package com.xyrisdev.mist.data.provider.mongo.config;

import com.xyrisdev.library.config.CachableConfiguration;
import org.jetbrains.annotations.NotNull;

public record MongoConfig(
		@NotNull String uri,
		@NotNull String database,
		@NotNull String users
) {

	public static @NotNull MongoConfig from(@NotNull CachableConfiguration config) {
		return new MongoConfig(
				config.getString("data.mongo.uri", "mongodb://localhost:27017"),
				config.getString("data.mongo.database", "mist_data"),
				config.getString("data.mongo.collections.users", "mist_users")
		);
	}
}
