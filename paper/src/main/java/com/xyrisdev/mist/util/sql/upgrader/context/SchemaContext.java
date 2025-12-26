/**
 * External utility class.
 * Maintained by Mist.
 */
package com.xyrisdev.mist.util.sql.upgrader.context;

import com.xyrisdev.mist.util.sql.upgrader.runnable.SQLRunnable;
import com.xyrisdev.mist.util.sql.upgrader.meta.SchemaMeta;
import com.xyrisdev.mist.util.sql.upgrader.table.SchemaTable;
import com.xyrisdev.mist.util.sql.upgrader.dialect.SqlDialect;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public class SchemaContext {

	private final Connection conn;

	private final SchemaMeta meta;
	private final SchemaTable table;

	private SchemaContext(Connection conn, SqlDialect dialect) {
		this.conn = conn;
		this.meta = new SchemaMeta(conn, dialect);
		this.table = new SchemaTable(conn, dialect);
	}

	public static @NotNull SchemaContext open(@NotNull Connection conn) throws SQLException {
		return new SchemaContext(conn, SqlDialect.detect(conn));
	}

	public void transaction(@NotNull SQLRunnable action) throws SQLException {
		final boolean auto = conn.getAutoCommit();

		conn.setAutoCommit(false);

		try {
			action.run();
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(auto);
		}
	}

	public SchemaMeta meta() {
		return meta;
	}

	public SchemaTable table() {
		return table;
	}
}
