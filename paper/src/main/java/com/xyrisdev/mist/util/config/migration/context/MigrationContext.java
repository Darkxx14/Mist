package com.xyrisdev.mist.util.config.migration.context;

import com.xyrisdev.library.lib.Library;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public record MigrationContext(
		@NotNull String file,
		@NotNull String version,
		@NotNull String backupPath,
		boolean backupEnabled
) {

	public MigrationContext {
		Objects.requireNonNull(file, "file");
		Objects.requireNonNull(version, "version");
		Objects.requireNonNull(backupPath, "backupPath");
	}

	@Contract(" -> new")
	public @NotNull File configFile() {
		return new File(
				Library.plugin().getDataFolder(),
				file
		);
	}

	@Contract(" -> new")
	public @NotNull File backupDirectory() {
		return new File(
				Library.plugin().getDataFolder(),
				backupPath
		);
	}
}
