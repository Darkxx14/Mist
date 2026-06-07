package com.xyrisdev.mist.hook.impl;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.xyrisdev.mist.hook.MistHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class EssentialsHook {

	private static @Nullable Essentials api;

	public static @NotNull MistHook hook() {
		return MistHook.builder()
				.plugin("Essentials")
				.onLoad(plugin -> api = (Essentials) plugin)
				.log(true)
				.success("Hooked into EssentialsX")
				.failure("EssentialsX not found")
				.build();
	}

	public static boolean enabled() {
		return api != null;
	}

	public static void ignore(
			@NotNull UUID senderId,
			@NotNull UUID targetId,
			boolean ignored
	) {
		if (!enabled()) {
			return;
		}

		try {
			final Essentials essentials = essentials();

			final User sender = essentials.getUser(senderId);
			final User target = essentials.getUser(targetId);

			if (sender == null || target == null) {
				return;
			}

			sender.setIgnoredPlayer(target, ignored);
		} catch (Exception ignore) {
			// ignore ts LOL
		}
	}

	public static @NotNull Essentials essentials() {
		if (api == null) {
			throw new IllegalStateException("EssentialsX not initialized");
		}

		return api;
	}
}