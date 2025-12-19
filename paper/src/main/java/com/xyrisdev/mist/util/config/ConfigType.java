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

	CHAT_FORMAT(
			"modules/chat_format.yml",
			new MigrationContext(
					"modules/chat_format.yml",
					"config_version",
					"backups",
					true
			)
	),

	CHAT_REPLACEMENTS(
			"modules/chat_replacement.yml",
			new MigrationContext(
					"modules/chat_replacement.yml",
					"config_version",
					"backups",
					true
			)
	),

	RENDER(
			"modules/render.yml",
			new MigrationContext(
					"modules/render.yml",
					"config_version",
					"backups",
					true
			)
	),

	FILTER(
			"modules/chat_filter.yml",
			new MigrationContext(
					"modules/chat_filter.yml",
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
