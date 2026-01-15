package com.xyrisdev.mist.data.provider.sql;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;
import com.xyrisdev.mist.data.DatabaseProvider;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

public abstract class SQLProvider implements DatabaseProvider {

	protected HikariDataSource dataSource;
	protected ChatUserRepository users;

	protected final CachableConfiguration config;

	protected SQLProvider(@NotNull CachableConfiguration config) {
		this.config = config;
	}

	@Override
	public ChatUserRepository users() {
		return users;
	}

	@Override
	public void shutdown() {
		if (dataSource != null) {
			dataSource.close();
		}
	}
}
