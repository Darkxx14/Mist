package com.xyrisdev.mist.modules.filter.config;

public record AntiFloodConfig(
		boolean enable,
		int maxLength,
		int maxRepeatedCharacters,
		boolean alertStaff
) {}
