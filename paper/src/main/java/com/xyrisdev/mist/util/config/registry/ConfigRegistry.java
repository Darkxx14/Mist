package com.xyrisdev.mist.util.config.registry;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.config.migration.ConfigMigrator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;

public class ConfigRegistry {

	private final EnumMap<ConfigType, CachableConfiguration> configs;

	private ConfigRegistry(EnumMap<ConfigType, CachableConfiguration> configs) {
		this.configs = configs;
	}

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

		return new ConfigRegistry(map);
	}

	public @NotNull CachableConfiguration get(@NotNull ConfigType type) {
		final CachableConfiguration config = configs.get(type);

		if (config == null) {
			throw new IllegalStateException(
					"Config not registered: " + type.name()
			);
		}

		return config;
	}

	public @NotNull List<String> all() {
		return configs.keySet()
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
		for (CachableConfiguration config : configs.values()) {
			config.reload();
		}
	}
}
