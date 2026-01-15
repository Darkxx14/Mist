package com.xyrisdev.mist.data.provider.sql.config;

import com.xyrisdev.library.config.CachableConfiguration;
import org.jetbrains.annotations.NotNull;

public class MySQLConfig {

	private final String host;
	private final int port;
	private final String database;
	private final String username;
	private final String password;
	private final boolean useSsl;
	private final String parameters;

	private MySQLConfig(
			@NotNull String host, int port,
			@NotNull String database, @NotNull String username,
			@NotNull String password, boolean useSsl,
			@NotNull String parameters
	) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.useSsl = useSsl;
		this.parameters = normalize(parameters);
	}

	public static @NotNull MySQLConfig from(@NotNull CachableConfiguration config) {
		return new MySQLConfig(
				config.getString("data.mysql.host", "127.0.0.1"),
				config.getInt("data.mysql.port", 3306),
				config.getString("data.mysql.database", "mist"),
				config.getString("data.mysql.username", "root"),
				config.getString("data.mysql.password", ""),
				config.getBoolean("data.mysql.use_ssl", false),
				config.getString(
						"data.mysql.parameters",
						"?useUnicode=true&characterEncoding=utf8"
				)
		);
	}

	public String jdbcUrl() {
		final String sslParam = parameters.contains("useSSL")
				? ""
				: "&useSSL=" + useSsl;

		return "jdbc:mysql://" + host + ":" + port + "/" + database +
				parameters + sslParam;
	}

	public String username() {
		return username;
	}

	public String password() {
		return password;
	}

	private static @NotNull String normalize(@NotNull String params) {
		if (params.isBlank()) {
			return "";
		}

		return params.startsWith("?") ? params : "?" + params;
	}
}
