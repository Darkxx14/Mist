/**
 * External utility class.
 * Maintained by Mist.
 */
package com.xyrisdev.mist.util.sql.upgrader.meta;

import com.xyrisdev.mist.util.sql.object.SQLSchemaDefinition;
import com.xyrisdev.mist.util.sql.upgrader.dialect.SqlDialect;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaMeta {

	private static final String TABLE = "mist_schema_meta";
	private static final String KEY = "schema_key";
	private static final String VALUE = "schema_value";

	private final Connection conn;
	private final SqlDialect dialect;

	public SchemaMeta(Connection conn, SqlDialect dialect) {
		this.conn = conn;
		this.dialect = dialect;
	}

	public int version(@NotNull SQLSchemaDefinition schema) throws SQLException {
		ensure();

		final String sql = "SELECT " + dialect.q(VALUE) +
				" FROM " + dialect.q(TABLE) +
				" WHERE " + dialect.q(KEY) + "=?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, schema.table() + "_version");

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next()
						? Integer.parseInt(rs.getString(1))
						: 0;
			}
		}
	}

	public void write(@NotNull SQLSchemaDefinition schema) throws SQLException {
		dialect.writeVersion(
				conn,
				schema.table() + "_version",
				String.valueOf(schema.version())
		);
	}

	private void ensure() throws SQLException {
		try (Statement st = conn.createStatement()) {
			st.execute(String.format(
					dialect.metaDDL(),
					TABLE, KEY, VALUE
			));
		}
	}
}
