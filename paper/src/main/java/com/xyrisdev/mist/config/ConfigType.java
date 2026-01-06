package com.xyrisdev.mist.config;

import com.xyrisdev.mist.config.migration.context.MigrationContext;
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
					"backup",
					true
			)
	),

	LANGUAGE(
			"language.yml",
			new MigrationContext(
					"language.yml",
					"config_version",
					"backup",
					true
			)
	),

	// extensions
	CHAT_FORMAT(
			"extension/chat_format.yml",
			new MigrationContext(
					"extension/chat_format.yml",
					"config_version",
					"backup",
					true
			)
	),

	CHAT_REPLACEMENTS(
			"extension/chat_replacement.yml",
			new MigrationContext(
					"extension/chat_replacement.yml",
					"config_version",
					"backup",
					true
			)
	),

	RENDER(
			"extension/render.yml",
			new MigrationContext(
					"extension/render.yml",
					"config_version",
					"backup",
					true
			)
	),

	CHAT_FILTER(
			"extension/chat_filter.yml",
			new MigrationContext(
					"extension/chat_filter.yml",
					"config_version",
					"backup",
					true
			)
	),

	CHAT_MENTIONS(
			"extension/chat_mentions.yml",
			new MigrationContext(
					"extension/chat_mentions.yml",
					"config_version",
					"backup",
					true
			)
	),

	// misc extensions
	ANNOUNCEMENTS(
			"misc/announcements.yml",
			new MigrationContext(
					"misc/announcements.yml",
					"config_version",
					"backup",
					true
			)
	),

	PRIVATE_MESSAGES(
			"misc/private_messages.yml",
			new MigrationContext(
					"misc/private_messages.yml",
					"config_version",
					"backup",
					true
			)
	),

	// internal
	LEETMAP(
			"internal/leetmap.yml",
			new MigrationContext(
					"internal/leetmap.yml",
					"config_version",
					"backup",
					true
			)
	),

	COMMANDS(
			"internal/commands.yml",
			new MigrationContext(
					"internal/commands.yml",
					"config_version",
					"backup",
					true
			)
	),

	PLACEHOLDERS(
			"internal/placeholders.yml",
			new MigrationContext(
					"internal/placeholders.yml",
					"config_version",
					"backup",
					true
			)
	);

	private final String path;
	private final MigrationContext migration;

	public Optional<MigrationContext> migration() {
		return Optional.ofNullable(migration);
	}
}
