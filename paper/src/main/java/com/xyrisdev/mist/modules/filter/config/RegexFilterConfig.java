package com.xyrisdev.mist.modules.filter.config;

import java.util.List;

public record RegexFilterConfig(
		boolean enable,
		boolean cancelMessage,
		char replaceWith,
		List<String> patterns
) {}
