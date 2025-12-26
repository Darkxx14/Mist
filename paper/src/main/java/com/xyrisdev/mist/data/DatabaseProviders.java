package com.xyrisdev.mist.data;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.data.provider.mongo.MongoProvider;
import com.xyrisdev.mist.data.provider.mongo.config.MongoConfig;
import com.xyrisdev.mist.data.provider.sql.*;
import com.xyrisdev.mist.data.provider.sql.config.MySQLConfig;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class DatabaseProviders {

	public static @NotNull DatabaseProvider create(@NotNull CachableConfiguration config) {
		final DatabaseType type = DatabaseType.parse(
				config.getString("data.type", "sqlite")
		);

		final Path dataDir = ChatPlugin.instance()
				.getDataFolder()
				.toPath();

		return switch (type) {
			case H2 -> new H2Provider(
					dataDir.resolve(config.getString("data.h2.file", "data/mist-h2")).normalize(),
					config
			);

			case SQLITE -> new SQLiteProvider(
					dataDir.resolve(config.getString("data.sqlite.file", "data/mist-sqlite.db")).normalize(),
					config
			);

			case MYSQL -> new MySQLProvider(
					MySQLConfig.from(config),
					config
			);

			case MONGO -> new MongoProvider(
					MongoConfig.from(config)
			);
		};
	}
}
