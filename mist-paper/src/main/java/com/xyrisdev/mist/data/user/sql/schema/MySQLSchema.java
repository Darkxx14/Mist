package com.xyrisdev.mist.data.user.sql.schema;

import com.xyrisdev.mist.util.sql.object.SQLColumn;
import com.xyrisdev.mist.util.sql.object.SQLSchemaDefinition;

import java.util.List;

public class MySQLSchema {

	public static final SQLSchemaDefinition USERS =
			new SQLSchemaDefinition(
					1,
					"mist_users",
					List.of(
							new SQLColumn("uuid", "VARCHAR(36) NOT NULL PRIMARY KEY"),
							new SQLColumn("data", "JSON NOT NULL")
					)
			);
}