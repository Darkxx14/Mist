package com.xyrisdev.mist.data;

import org.jetbrains.annotations.NotNull;

public enum DatabaseType {
	H2, SQLITE, MYSQL, MONGO;

	public static DatabaseType parse(@NotNull String raw) {
		if (raw.isBlank()) {
			throw new IllegalArgumentException("Database type is not provided.");
		}

		final String normalized = raw
				.toLowerCase()
				.replace("_", "")
				.replace("-", "")
				.replace(" ", "");

		return switch (normalized) {
			case "h2" -> H2;
			case "sqlite", "sqllite" -> SQLITE;
			case "mysql", "mysqldb", "mariadb", "maria" -> MYSQL;
			case "mongo", "mongodb" -> MONGO;
			default -> throw new IllegalArgumentException("Unknown database type: " + raw);
		};
	}
}
