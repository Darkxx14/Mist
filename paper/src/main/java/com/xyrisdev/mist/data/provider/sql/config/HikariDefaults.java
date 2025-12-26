package com.xyrisdev.mist.data.provider.sql.config;

import com.xyrisdev.library.config.CachableConfiguration;
import com.zaxxer.hikari.HikariConfig;
import org.jetbrains.annotations.NotNull;

public class HikariDefaults {

	public static void apply(@NotNull HikariConfig hikari, @NotNull CachableConfiguration config) {
		hikari.setMaximumPoolSize(
				config.getInt("data.pool.size", 10)
		);

		hikari.setMinimumIdle(
				config.getInt("data.pool.min_idle", 2)
		);

		hikari.setMaxLifetime(
				longSafe(config, "data.pool.max_lifetime_ms", 1_800_000L)
		);

		hikari.setConnectionTimeout(
				longSafe(config, "data.pool.connection_timeout_ms", 5_000L)
		);
	}

	private static long longSafe(@NotNull CachableConfiguration config, @NotNull String path, long def) {
		try {
			return config.getLong(path, def);
		} catch (ClassCastException ignored) {
			return config.getInt(path, (int) def);
		}
	}
}
