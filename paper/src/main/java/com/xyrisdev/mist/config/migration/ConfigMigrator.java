package com.xyrisdev.mist.config.migration;

import com.xyrisdev.library.lib.Library;
import com.xyrisdev.mist.config.migration.context.MigrationContext;
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
	private final MigrationContext ctx;

	public ConfigMigrator(@NotNull MigrationContext ctx) {
		this.ctx = Objects.requireNonNull(ctx, "ctx");
		this.logger = Library.plugin().getLogger();
	}

	public boolean migrate() {
		final YamlConfiguration latest = latest();
		final YamlConfiguration current = current();

		final int currentVersion = current.getInt(ctx.version(), 0);
		final int latestVersion = latest.getInt(ctx.version(), 0);

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

			if (key.equals(ctx.version())) {
				continue;
			}

			if (target.contains(key)) {
				target.set(key, source.get(key));
			}
		}
	}

	private void backup(int version) {
		if (!ctx.backupEnabled() || !ctx.configFile().exists()) {
			return;
		}

		final File backupDir = ctx.backupDirectory();

		if (!backupDir.exists() && !backupDir.mkdirs()) {
			logger.warning("Failed to create config backup directory");
			return;
		}

		final Path target = new File(backupDir, ctx.file() + "_" + version + ".yml").toPath();

		try {
			Files.copy(
					ctx.configFile().toPath(),
					target,
					StandardCopyOption.REPLACE_EXISTING
			);
		} catch (IOException e) {
			logger.warning("Failed to backup config: " + e.getMessage());
		}
	}

	private void save(@NotNull YamlConfiguration config) {
		try {
			config.save(ctx.configFile());
		} catch (IOException e) {
			throw new IllegalStateException(
					"Failed to save migrated config",
					e
			);
		}
	}

	private @NotNull YamlConfiguration latest() {
		final InputStream stream = Library.plugin().getResource(ctx.file());

		if (stream == null) {
			throw new IllegalStateException(
					"Missing config resource: " + ctx.file()
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
		if (!ctx.configFile().exists()) {
			Library.plugin().saveResource(ctx.file(), false);
		}

		return YamlConfiguration.loadConfiguration(ctx.configFile());
	}
}
