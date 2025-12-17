package com.xyrisdev.mist.modules.filter.config;

public record FilterConfig(
		boolean enabled,
		SimilarityConfig similarity,
		AntiFloodConfig antiFlood,
		AntiCapsConfig antiCaps
) {}
