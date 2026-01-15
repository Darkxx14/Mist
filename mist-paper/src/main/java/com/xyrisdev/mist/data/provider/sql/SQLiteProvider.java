package com.xyrisdev.mist.data.provider.sql;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.data.provider.sql.config.HikariDefaults;
import com.xyrisdev.mist.data.user.sql.repository.SQLiteChatUserRepository;
import com.xyrisdev.mist.data.user.sql.schema.SQLiteSchema;
import com.xyrisdev.mist.util.sql.upgrader.SchemaUpgrader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class SQLiteProvider extends SQLProvider {

	private final Path file;

	public SQLiteProvider(@NotNull Path file, @NotNull CachableConfiguration config) {
		super(config);
		this.file = file;
	}

	@Override
	public void connect() {
		final HikariConfig config = new HikariConfig();

		config.setJdbcUrl("jdbc:sqlite:" + file.toAbsolutePath());
		config.setDriverClassName("org.sqlite.JDBC");
		config.setPoolName("mist-pool");

		HikariDefaults.apply(config, this.config);

		this.dataSource = new HikariDataSource(config);

		SchemaUpgrader.upgrade(
				dataSource,
				SQLiteSchema.USERS
		);

		this.users = new SQLiteChatUserRepository(
				dataSource,
				SQLiteSchema.USERS.table()
		);
	}
}
