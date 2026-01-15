package com.xyrisdev.mist.data.provider.sql;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.data.provider.sql.config.HikariDefaults;
import com.xyrisdev.mist.data.provider.sql.config.MySQLConfig;
import com.xyrisdev.mist.data.user.sql.repository.MySQLChatUserRepository;
import com.xyrisdev.mist.data.user.sql.schema.MySQLSchema;
import com.xyrisdev.mist.util.sql.upgrader.SchemaUpgrader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

public class MySQLProvider extends SQLProvider {

	private final MySQLConfig mysql;

	public MySQLProvider(@NotNull MySQLConfig mysql, @NotNull CachableConfiguration config) {
		super(config);
		this.mysql = mysql;
	}

	@Override
	public void connect() {
		final HikariConfig hikari = new HikariConfig();

		hikari.setJdbcUrl(mysql.jdbcUrl());
		hikari.setUsername(mysql.username());
		hikari.setPassword(mysql.password());
		hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikari.setPoolName("mist-pool");

		HikariDefaults.apply(hikari, this.config);

		this.dataSource = new HikariDataSource(hikari);

		SchemaUpgrader.upgrade(
				dataSource,
				MySQLSchema.USERS
		);

		this.users = new MySQLChatUserRepository(
				dataSource,
				MySQLSchema.USERS.table()
		);
	}
}
