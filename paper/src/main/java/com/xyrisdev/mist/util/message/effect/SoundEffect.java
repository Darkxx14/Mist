package com.xyrisdev.mist.util.message.effect;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
@UtilityClass
public class SoundEffect {

	public static void play(@NotNull Player player, @NotNull ConfigurationSection section) {
		final String keyString = section.getString("key");

		if (keyString == null || keyString.isBlank()) {
			return;
		}

		final Sound.Source source = source(
				section.getString("source", "master")
		);

		final float volume = (float) section.getDouble("volume", 1.0);
		final float pitch = (float) section.getDouble("pitch", 1.0);

		final Sound sound = Sound.sound(
				Key.key(keyString),
				source,
				volume,
				pitch
		);

		player.playSound(sound);
	}

	private static Sound.Source source(@NotNull String value) {
		try {
			return Sound.Source.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException ignored) {
			return Sound.Source.MASTER;
		}
	}
}
