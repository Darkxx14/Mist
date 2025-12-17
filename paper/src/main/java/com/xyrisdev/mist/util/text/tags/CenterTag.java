package com.xyrisdev.mist.util.text.tags;

import com.xyrisdev.mist.util.text.align.TextAligner;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import org.jetbrains.annotations.NotNull;

public final class CenterTag implements Modifying {

	private final Audience audience;

	public CenterTag(@NotNull Audience audience) {
		this.audience = audience;
	}

	@Override
	public @NotNull Component apply(@NotNull Component current, int depth) {
		if (depth != 0) {
			return Component.empty();
		}

		return TextAligner.center(this.audience, current);
	}
}
