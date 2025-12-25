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

	LANGUAGE(
			"language.yml",
			new MigrationContext(
					"language.yml",
					"config_version",
					"backups",
					true
			)
	),

	// extensions
	CHAT_FORMAT(
			"extension/chat_format.yml",
			new MigrationContext(
					"extension/chat_format.yml",
					"config_version",
					"backups",
					true
			)
	),

	CHAT_REPLACEMENTS(
			"extension/chat_replacement.yml",
			new MigrationContext(
					"extension/chat_replacement.yml",
					"config_version",
					"backups",
					true
			)
	),

	RENDER(
			"extension/render.yml",
			new MigrationContext(
					"extension/render.yml",
					"config_version",
					"backups",
					true
			)
	),

	CHAT_FILTER(
			"extension/chat_filter.yml",
			new MigrationContext(
					"extension/chat_filter.yml",
					"config_version",
					"backups",
					true
			)
	),

	// misc extensions
	ANNOUNCEMENTS(
			"misc/announcements.yml",
			new MigrationContext(
					"misc/announcements.yml",
					"config_version",
					"backups",
					true
			)
	),

	// internal
	LEETMAP(
			"internal/leetmap.yml",
			new MigrationContext(
					"internal/leetmap.yml",
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
