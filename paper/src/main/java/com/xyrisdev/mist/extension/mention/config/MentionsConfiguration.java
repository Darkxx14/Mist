package com.xyrisdev.mist.extension.mention.config;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public record MentionsConfiguration(
		boolean enabled,
		@NotNull Pattern pattern,
		@NotNull String format
) {}