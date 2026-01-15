package com.xyrisdev.mist.util.message.builder.object;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public enum MessageType {
	CHAT, ACTION_BAR, TITLE;

	public static Set<MessageType> parse(@Nullable String raw) {
		if (raw == null || raw.isBlank()) {
			return EnumSet.of(CHAT);
		}

		final EnumSet<MessageType> set = EnumSet.noneOf(MessageType.class);

		for (String part : raw.split(",")) {
			try {
				set.add(MessageType.valueOf(
								part.trim()
										.toUpperCase()
										.replace(' ', '_')
						)
				);
			} catch (IllegalArgumentException ignored) {
				// ignore
			}
		}

		return set.isEmpty() ? EnumSet.of(CHAT) : set;
	}
}
