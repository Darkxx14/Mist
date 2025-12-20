package com.xyrisdev.mist.util.config.migration;

import com.xyrisdev.library.lib.Library;
import com.xyrisdev.mist.util.config.migration.context.MigrationContext;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Logger;

public class ConfigMigrator {

	private final Logger logger;
	private final MigrationContext context;

	public ConfigMigrator(@NotNull MigrationContext context) {
		this.context = Objects.requireNonNull(context, "context");
		this.logger = Library.plugin().getLogger();
	}

	public boolean migrate() {
		final YamlConfiguration latest = latest();
		final YamlConfiguration current = current();

		final int currentVersion = current.getInt(context.version(), 0);
		final int latestVersion = latest.getInt(context.version(), 0);

		if (currentVersion >= latestVersion) {
			return false;
		}

		logger.info(() ->
				"Config outdated (" + currentVersion + " -> " + latestVersion + "), migrating..."
		);

		backup(currentVersion);
		merge(current, latest);
		save(latest);

		logger.info("Config migrated successfully.");
		return true;
	}

	private void merge(@NotNull YamlConfiguration source, @NotNull YamlConfiguration target) {
		for (String key : source.getKeys(true)) {

			if (key.equals(context.version())) {
				continue;
			}

			if (target.contains(key)) {
				target.set(key, source.get(key));
			}
		}
	}

	private void backup(int version) {
		if (!context.backupEnabled() || !context.configFile().exists()) {
			return;
		}

		final File backupDir = context.backupDirectory();

		if (!backupDir.exists() && !backupDir.mkdirs()) {
			logger.warning("Failed to create config backup directory");
			return;
		}

		final Path target = new File(backupDir, context.file() + "_" + version + ".yml").toPath();

		try {
			Files.copy(
					context.configFile().toPath(),
					target,
					StandardCopyOption.REPLACE_EXISTING
			);
		} catch (IOException e) {
			logger.warning("Failed to backup config: " + e.getMessage());
		}
	}

	private void save(@NotNull YamlConfiguration config) {
		try {
			config.save(context.configFile());
		} catch (IOException e) {
			throw new IllegalStateException(
					"Failed to save migrated config",
					e
			);
		}
	}

	private @NotNull YamlConfiguration latest() {
		final InputStream stream = Library.plugin().getResource(context.file());

		if (stream == null) {
			throw new IllegalStateException(
					"Missing config resource: " + context.file()
			);
		}

		try (Reader reader = new InputStreamReader(stream)) {
			return YamlConfiguration.loadConfiguration(reader);
		} catch (IOException e) {
			throw new IllegalStateException(
					"Failed to load latest config",
					e
			);
		}
	}

	private @NotNull YamlConfiguration current() {
		if (!context.configFile().exists()) {
			Library.plugin().saveResource(context.file(), false);
		}

		return YamlConfiguration.loadConfiguration(context.configFile());
	}
}
