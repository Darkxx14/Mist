package com.xyrisdev.mist.config.migration;

import com.xyrisdev.library.lib.Library;
import com.xyrisdev.mist.config.migration.context.MigrationContext;
import com.xyrisdev.mist.util.logger.MistLogger;
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

public record ConfigMigrator(@NotNull MigrationContext ctx) {

	public void migrate() {
		final YamlConfiguration latest = latest();
		final YamlConfiguration current = current();

		final int currentVersion = current.getInt(ctx.version(), 0);
		final int latestVersion = latest.getInt(ctx.version(), 0);

		if (currentVersion >= latestVersion) {
			return;
		}

		MistLogger.info(ctx.file() + " outdated (v" + currentVersion + " -> v" + latestVersion + "), migrating...");

		backup(currentVersion);
		merge(current, latest);
		save(latest);

		MistLogger.info(ctx.file() + " migrated successfully.");
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
			MistLogger.warn("Failed to create config backup directory");
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
			MistLogger.warn("Failed to backup " + ctx.file() + " -> " + e.getMessage());
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
