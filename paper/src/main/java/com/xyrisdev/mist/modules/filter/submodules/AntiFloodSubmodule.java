package com.xyrisdev.mist.modules.filter.submodules;

import com.xyrisdev.mist.modules.filter.config.AntiFloodConfig;
import com.xyrisdev.mist.modules.filter.object.FilterResult;
import com.xyrisdev.mist.modules.filter.object.FilterSubmodule;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class AntiFloodSubmodule implements FilterSubmodule {

	private final AntiFloodConfig config;

	public AntiFloodSubmodule(AntiFloodConfig config) {
		this.config = config;
	}

	@Override
	public @NotNull FilterResult handle(@NotNull UUID id, @NotNull String message) {
		if (message.length() > config.maxLength()) {
			return FilterResult.block();
		}

		if (hasRepeated(message, config.maxRepeatedCharacters())) {
			return FilterResult.block();
		}

		return FilterResult.pass();
	}

	private boolean hasRepeated(@NotNull String s, int max) {
		int count = 1;

		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == s.charAt(i - 1)) {
				if (++count > max) return true;
			} else {
				count = 1;
			}
		}

		return false;
	}

	@Override
	public int priority() {
		return 2;
	}

	@Override
	public boolean enabled() {
		return config.enable();
	}
}
