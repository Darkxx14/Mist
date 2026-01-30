package com.xyrisdev.mist.hook.impl;

import com.xyrisdev.mist.hook.MistHook;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LuckPermsHook {

	private static @Nullable LuckPerms api;

	public static @NotNull MistHook hook() {
		return MistHook.builder()
				.plugin("LuckPerms")
				.onLoad(l -> api = Bukkit.getServicesManager().load(LuckPerms.class))
				.onFail(() -> {
					throw new IllegalStateException("LuckPerms is required");
				})
				.log(true)
				.success("Hooked into LuckPerms")
				.failure("LuckPerms not found")
				.build();
	}

	public static @NotNull LuckPerms luckPerms() {
		if (api == null) {
			throw new IllegalStateException("LuckPerms not initialized");
		}

		return api;
	}
}
