package com.xyrisdev.mist.modules.filter.config;

import java.util.Set;

public record WordFilterConfig(
		boolean enable,
		String mode, // classic | aggressive
		double similarityThreshold,
		boolean cancelMessage,
		char replaceWith,
		Set<String> blockedWords
) {}
