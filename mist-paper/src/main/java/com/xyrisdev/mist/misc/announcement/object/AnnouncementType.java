package com.xyrisdev.mist.misc.announcement.object;

import org.jetbrains.annotations.NotNull;

public enum AnnouncementType {

	RANDOM, SEQUENTIAL;

	public static @NotNull AnnouncementType parse(@NotNull String raw) {
		if (raw.isBlank()) {
			return RANDOM;
		}

		return switch (raw.trim().toLowerCase()) {
			case "sequential", "order", "ordered" -> SEQUENTIAL;
			default -> RANDOM;
		};
	}
}
