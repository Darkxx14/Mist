package com.xyrisdev.mist.modules.filter.submodules;

import com.xyrisdev.mist.modules.filter.config.AntiCapsConfig;
import com.xyrisdev.mist.modules.filter.object.FilterResult;
import com.xyrisdev.mist.modules.filter.object.FilterSubmodule;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class AntiCapsSubmodule implements FilterSubmodule {

	private final AntiCapsConfig config;

	public AntiCapsSubmodule(AntiCapsConfig config) {
		this.config = config;
	}

	@Override
	public @NotNull FilterResult handle(@NotNull UUID uuid, @NotNull String message) {
		int caps = 0;

		for (char c : message.toCharArray()) {
			if (Character.isUpperCase(c)) {
				caps++;
			}
		}

		if (caps > config.maxCaps()) {
			return FilterResult.modify(message.toLowerCase());
		}

		return FilterResult.pass();
	}

	@Override
	public int priority() {
		return 3;
	}

	@Override
	public boolean enabled() {
		return config.enable();
	}
}
