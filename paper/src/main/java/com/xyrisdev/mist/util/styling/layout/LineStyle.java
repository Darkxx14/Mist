package com.xyrisdev.mist.util.styling.layout;

import com.xyrisdev.mist.util.styling.color.MistColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public enum LineStyle {

	PLAIN(UnaryOperator.identity()),
	GRADIENT(MistColor::gradient),
	SUCCESS(MistColor::success),
	ERROR(MistColor::error),
	WARNING(MistColor::warning),
	INFO(MistColor::info),
	MUTED(s -> "<dark_gray>" + s + "</dark_gray>");

	private final UnaryOperator<String> formatter;

	LineStyle(@NotNull UnaryOperator<String> formatter) {
		this.formatter = formatter;
	}

	public String apply(@NotNull String input) {
		return formatter.apply(input);
	}
}
