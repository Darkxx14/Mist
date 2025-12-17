package com.xyrisdev.mist.util.styling.layout;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record LinePart(
		@NotNull LineStyle style,
		@NotNull String content
) {
	@Contract("_, _ -> new")
	public static @NotNull LinePart part(@NotNull LineStyle style, @NotNull String content) {
		return new LinePart(style, content);
	}
}
