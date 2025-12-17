package com.xyrisdev.mist.module.format.entry;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record FormatEntry(
		@NotNull String message,
		List<String> hoverText,
		ClickAction action
) {}
