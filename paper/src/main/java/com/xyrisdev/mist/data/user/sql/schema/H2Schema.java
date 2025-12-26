package com.xyrisdev.mist.data.user.sql.schema;

import com.xyrisdev.mist.util.sql.object.SQLColumn;
import com.xyrisdev.mist.util.sql.object.SQLSchemaDefinition;

import java.util.List;

public class H2Schema {

	public static final SQLSchemaDefinition USERS =
			new SQLSchemaDefinition(
					1,
					"mist_users",
					List.of(
							new SQLColumn("uuid", "uuid VARCHAR(36) NOT NULL PRIMARY KEY"),
							new SQLColumn("data", "data CLOB NOT NULL")
					)
			);
}
