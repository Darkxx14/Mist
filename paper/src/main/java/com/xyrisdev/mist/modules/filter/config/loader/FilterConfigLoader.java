package com.xyrisdev.mist.modules.filter.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.modules.filter.config.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class FilterConfigLoader {

	private FilterConfigLoader() {}

	@Contract("_ -> new")
	public static @NotNull FilterConfig load(@NotNull CachableConfiguration config) {
		return new FilterConfig(
				config.getBoolean("enabled", true),

				new SimilarityConfig(
						config.getBoolean("similarity-check.enable", true),
						config.getDouble("similarity-check.threshold", 0.75D)
				),

				new AntiFloodConfig(
						config.getBoolean("anti-flood.enable", true),
						config.getInt("anti-flood.max-length", 80),
						config.getInt("anti-flood.max-repeated-characters", 4),
						config.getBoolean("anti-flood.alert-staff", true)
				),

				new AntiCapsConfig(
						config.getBoolean("anti-caps.enable", true),
						config.getInt("anti-caps.max-caps", 5)
				)
		);
	}
}
