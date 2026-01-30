package com.xyrisdev.mist.util.time;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Locale;

@UtilityClass
public final class DurationParser {

	private static final Duration DEFAULT = Duration.ofMinutes(5);
	private static final long TICKS_PER_SECOND = 20L;

	public static @NotNull Duration parse(@NotNull String raw) {
		if (raw.isBlank()) {
			return DEFAULT;
		}

		final String input = raw.trim().toLowerCase(Locale.ROOT);

		if (input.length() >= 2 && Character.isDigit(input.charAt(0))) {
			final char unit = input.charAt(input.length() - 1);

			if (Character.isLetter(unit)) {
				final long value = parseSafe(input.substring(0, input.length() - 1));

				if (value > 0) {
					return switch (unit) {
						case 's' -> Duration.ofSeconds(value);
						case 'm' -> Duration.ofMinutes(value);
						case 'h' -> Duration.ofHours(value);
						case 'd' -> Duration.ofDays(value);
						case 'w' -> Duration.ofDays(value * 7);
						default -> DEFAULT;
					};
				}
			}
		}

		final String[] parts = input.split("\\s+");
		if (parts.length == 2) {
			final long value = parseSafe(parts[0]);

			if (value > 0) {
				return switch (parts[1]) {
					case "second", "seconds", "sec", "secs" ->
							Duration.ofSeconds(value);

					case "minute", "minutes", "min", "mins" ->
							Duration.ofMinutes(value);

					case "hour", "hours", "hr", "hrs" ->
							Duration.ofHours(value);

					case "day", "days" ->
							Duration.ofDays(value);

					case "week", "weeks" ->
							Duration.ofDays(value * 7);

					default -> DEFAULT;
				};
			}
		}

		return DEFAULT;
	}

	public static long toTicks(@NotNull Duration duration) {
		if (duration.isZero() || duration.isNegative()) {
			return 0L;
		}

		return duration.toSeconds() * TICKS_PER_SECOND;
	}

	public @NotNull Duration fromTicks(long ticks) {
		if (ticks <= 0) {
			return Duration.ZERO;
		}

		return Duration.ofSeconds(ticks / TICKS_PER_SECOND);
	}

	private long parseSafe(String value) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException ignored) {
			return -1;
		}
	}
}
