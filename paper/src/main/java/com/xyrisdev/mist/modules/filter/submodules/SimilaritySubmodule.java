package com.xyrisdev.mist.modules.filter.submodules;

import com.xyrisdev.mist.modules.filter.config.SimilarityConfig;
import com.xyrisdev.mist.modules.filter.object.FilterResult;
import com.xyrisdev.mist.modules.filter.object.FilterSubmodule;
import com.xyrisdev.mist.util.SimilarityUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SimilaritySubmodule implements FilterSubmodule {

	private final SimilarityConfig config;
	private final Map<UUID, String> lastMessages = new ConcurrentHashMap<>();

	public SimilaritySubmodule(SimilarityConfig config) {
		this.config = config;
	}

	@Override
	public @NotNull FilterResult handle(@NotNull UUID senderId, @NotNull String message) {
		final String last = lastMessages.put(senderId, message);

		if (last == null) {
			return FilterResult.pass();
		}

		final double similarity = SimilarityUtil.similarity(last, message);

		if (similarity >= config.threshold()) {
			return FilterResult.block();
		}

		return FilterResult.pass();
	}

	@Override
	public int priority() {
		return 1;
	}

	@Override
	public boolean enabled() {
		return config.enable();
	}
}
