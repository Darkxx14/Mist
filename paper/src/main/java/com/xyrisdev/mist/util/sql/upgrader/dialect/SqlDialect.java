package com.xyrisdev.mist.util.sql.upgrader.dialect;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

public enum SqlDialect {

	MYSQL("`") {
		@Override
		public String metaDDL() {
			return """
				CREATE TABLE IF NOT EXISTS `%s` (
				  `%s` VARCHAR(128) PRIMARY KEY,
				  `%s` VARCHAR(32) NOT NULL
				) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
				""";
		}

		@Override
		public void writeVersion(Connection c, String k, String v) throws SQLException {
			exec(
					c,
					"INSERT INTO %s (%s,%s) VALUES (?,?) " +
							"ON DUPLICATE KEY UPDATE %s=?",
					k, v, v
			);
		}
	},

	SQLITE("`") {
		@Override
		public String metaDDL() {
			return """
				CREATE TABLE IF NOT EXISTS `%s` (
				  `%s` TEXT PRIMARY KEY,
				  `%s` TEXT NOT NULL
				)
				""";
		}

		@Override
		public String normalize(String sql) {
			return sql.replace("VARCHAR", "TEXT")
					.replace("CLOB", "TEXT");
		}

		@Override
		public void writeVersion(Connection c, String k, String v) throws SQLException {
			exec(
					c,
					"INSERT OR REPLACE INTO %s (%s,%s) VALUES (?,?)",
					k, v
			);
		}
	},

	H2("") {
		@Override
		public String metaDDL() {
			return """
				CREATE TABLE IF NOT EXISTS %s (
				  %s VARCHAR(128) PRIMARY KEY,
				  %s VARCHAR(32) NOT NULL
				)
				""";
		}

		@Override
		public void writeVersion(Connection c, String k, String v) throws SQLException {
			exec(
					c,
					"MERGE INTO %s KEY (%s) VALUES (?,?)",
					k, v
			);
		}
	};

	private final String quote;

	SqlDialect(String quote) {
		this.quote = quote;
	}

	public static SqlDialect detect(@NotNull Connection conn) throws SQLException {
		final String product =
				conn.getMetaData()
						.getDatabaseProductName()
						.toLowerCase(Locale.ROOT);

		if (product.contains("mysql") || product.contains("mariadb")) {
			return MYSQL;
		}
		if (product.contains("sqlite")) {
			return SQLITE;
		}

		return H2;
	}

	public String q(String id) {
		return this == H2 && id.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")
				? id
				: quote + id + quote;
	}

	public String normalize(String sql) {
		return sql;
	}

	public abstract String metaDDL();

	public abstract void writeVersion(Connection c, String k, String v)
			throws SQLException;

	protected void exec(
			Connection c, String sqlFormat,
			String key, String value, String @NotNull ... extra
	) throws SQLException {

		String sql = String.format(
				sqlFormat,
				q("mist_schema_meta"),
				q("schema_key"),
				q("schema_value"),
				q("schema_value")
		);

		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, key);
			ps.setString(2, value);

			if (extra.length > 0) {
				ps.setString(3, extra[0]);
			}

			ps.executeUpdate();
		}
	}
}
