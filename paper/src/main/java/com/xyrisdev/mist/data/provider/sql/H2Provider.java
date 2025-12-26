package com.xyrisdev.mist.data.provider.sql;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.data.provider.sql.config.HikariDefaults;
import com.xyrisdev.mist.data.user.sql.repository.H2ChatUserRepository;
import com.xyrisdev.mist.data.user.sql.schema.H2Schema;
import com.xyrisdev.mist.util.sql.upgrader.SchemaUpgrader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class H2Provider extends SQLProvider {

	private final Path file;

	public H2Provider(@NotNull Path file, @NotNull CachableConfiguration config) {
		super(config);
		this.file = file;
	}

	@Override
	public void connect() {
		final HikariConfig config = new HikariConfig();

		config.setJdbcUrl(
				"jdbc:h2:file:" + file.toAbsolutePath() + ";AUTO_SERVER=TRUE"
		);

		config.setDriverClassName("org.h2.Driver");
		config.setPoolName("mist-pool");

		HikariDefaults.apply(config, this.config);

		this.dataSource = new HikariDataSource(config);

		SchemaUpgrader.upgrade(
				dataSource,
				H2Schema.USERS
		);

		this.users = new H2ChatUserRepository(
				dataSource,
				H2Schema.USERS.table()
		);
	}
}
