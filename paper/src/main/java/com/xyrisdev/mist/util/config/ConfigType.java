package com.xyrisdev.mist.util.config;

import com.xyrisdev.mist.util.config.migration.context.MigrationContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum ConfigType {

	CONFIGURATION(
			"configuration.yml",
			new MigrationContext(
					"configuration.yml",
					"config_version",
					"backups",
					true
			)
	),

	CHAT_FILTERS(
			"modules/chat_filters.yml",
			new MigrationContext(
					"modules/chat_filters.yml",
					"config_version",
					"backups",
					true
			)
	);

	private final String path;
	private final MigrationContext migration;

	public Optional<MigrationContext> migration() {
		return Optional.ofNullable(migration);
	}
}
