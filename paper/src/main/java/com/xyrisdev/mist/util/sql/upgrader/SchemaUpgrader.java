/**
 * External utility class.
 * Maintained by Mist.
 */
package com.xyrisdev.mist.util.sql.upgrader;

import com.xyrisdev.mist.util.sql.object.SQLSchemaDefinition;
import com.xyrisdev.mist.util.sql.upgrader.context.SchemaContext;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class SchemaUpgrader {

	private static final Logger log = LoggerFactory.getLogger(SchemaUpgrader.class);

	public static void upgrade(@NotNull DataSource dataSource, SQLSchemaDefinition schema) {
		try (Connection conn = dataSource.getConnection()) {
			SchemaContext ctx = SchemaContext.open(conn);

			ctx.transaction(() -> {
				int current = ctx.meta().version(schema);

				if (current >= schema.version()) {
					return;
				}

				if (!ctx.table().exists(schema.table())) {
					ctx.table().create(schema);
					ctx.meta().write(schema);
					return;
				}

				ctx.table().addMissingColumns(schema);
				ctx.meta().write(schema);
			});
		} catch (SQLException e) {
			log.error("Schema upgrade failed", e);
			throw new IllegalStateException("Schema upgrade failed", e);
		}
	}
}
