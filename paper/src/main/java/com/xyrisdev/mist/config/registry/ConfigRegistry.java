package com.xyrisdev.mist.config.registry;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.config.migration.ConfigMigrator;
import com.xyrisdev.mist.util.logger.MistLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("unused")
public record ConfigRegistry(
		@NotNull EnumMap<ConfigType,
		@NotNull CachableConfiguration> configs
) {

	@Contract(" -> new")
	public static @NotNull ConfigRegistry load() {
		final EnumMap<ConfigType, CachableConfiguration> map = new EnumMap<>(ConfigType.class);

		final CachableConfiguration baseConfig = CachableConfiguration.builder()
				.file(ConfigType.CONFIGURATION.getPath())
				.build();

		final boolean migrationEnabled = baseConfig.getBoolean("config_migration.enabled", true);

		for (ConfigType type : ConfigType.values()) {
			if (migrationEnabled) {
				type.migration().ifPresent(ctx -> new ConfigMigrator(ctx).migrate());
			}

			map.put(
					type,
					CachableConfiguration.builder()
							.file(type.getPath())
							.build()
			);
		}

		MistLogger.info("Loaded " + map.size() + " configuration files");
		return new ConfigRegistry(map);
	}

	public @NotNull CachableConfiguration get(@NotNull ConfigType type) {
		final CachableConfiguration config = this.configs.get(type);

		if (config == null) {
			throw new IllegalStateException(
					"Config not registered: " + type.name()
			);
		}

		return config;
	}

	public @NotNull @Unmodifiable List<String> all() {
		return this.configs.keySet()
				.stream()
				.map(ConfigType::getPath)
				.map(Object::toString)
				.sorted()
				.toList();
	}

	public void reload(@NotNull ConfigType type) {
		get(type).reload();
	}

	public void reloadAll() {
		for (CachableConfiguration config : this.configs.values()) {
			config.reload();
		}
	}
}
