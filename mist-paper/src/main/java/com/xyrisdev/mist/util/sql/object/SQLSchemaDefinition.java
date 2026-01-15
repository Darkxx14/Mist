/**
 * External utility class.
 * Maintained by Mist.
 */
package com.xyrisdev.mist.util.sql.object;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SQLSchemaDefinition(
		int version,
		@NotNull String table,
		@NotNull List<SQLColumn> columns
) {}
