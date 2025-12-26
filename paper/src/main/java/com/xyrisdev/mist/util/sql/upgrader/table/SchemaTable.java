package com.xyrisdev.mist.util.sql.upgrader.table;

import com.xyrisdev.mist.util.sql.object.SQLColumn;
import com.xyrisdev.mist.util.sql.object.SQLSchemaDefinition;
import com.xyrisdev.mist.util.sql.upgrader.dialect.SqlDialect;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class SchemaTable {

	private final Connection conn;
	private final SqlDialect dialect;

	public SchemaTable(Connection conn, SqlDialect dialect) {
		this.conn = conn;
		this.dialect = dialect;
	}

	public boolean exists(String table) throws SQLException {
		final DatabaseMetaData meta = conn.getMetaData();
		final String schema = conn.getSchema();

		try (ResultSet rs = meta.getTables(
				null,
				schema,
				"%",
				new String[]{"TABLE"}
		)) {
			while (rs.next()) {
				String name = rs.getString("TABLE_NAME");
				if (name != null && name.equalsIgnoreCase(table)) {
					return true;
				}
			}
			return false;
		}
	}

	public void create(@NotNull SQLSchemaDefinition schema) throws SQLException {
		final StringBuilder ddl = new StringBuilder("CREATE TABLE ")
				.append(dialect.q(schema.table()))
				.append(" (");

		boolean first = true;

		for (SQLColumn c : schema.columns()) {
			if (!first) ddl.append(", ");

			ddl.append(dialect.q(c.name()))
					.append(" ")
					.append(dialect.normalize(c.sql()));

			first = false;
		}

		ddl.append(")");

		try (Statement st = conn.createStatement()) {
			st.execute(ddl.toString());
		}
	}

	public void addMissingColumns(SQLSchemaDefinition schema) throws SQLException {
		final Set<String> existing = new HashSet<>();

		try (ResultSet rs = conn.getMetaData()
				.getColumns(null, conn.getSchema(), "%", null)) {

			while (rs.next()) {
				final String tableName = rs.getString("TABLE_NAME");

				if (!tableName.equalsIgnoreCase(schema.table())) {
					continue;
				}

				existing.add(
						rs.getString("COLUMN_NAME")
								.toLowerCase(Locale.ROOT)
				);
			}
		}

		for (SQLColumn c : schema.columns()) {
			if (existing.contains(c.name().toLowerCase(Locale.ROOT))) {
				continue;
			}

			final String ddl = "ALTER TABLE " +
					dialect.q(schema.table()) +
					" ADD COLUMN " +
					dialect.q(c.name()) +
					" " +
					dialect.normalize(c.sql());

			try (Statement st = conn.createStatement()) {
				st.execute(ddl);
			}
		}
	}
}
