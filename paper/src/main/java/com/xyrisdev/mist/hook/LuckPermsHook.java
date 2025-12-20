package com.xyrisdev.mist.hook;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;

import java.util.Objects;

public class LuckPermsHook {

	private static LuckPerms api;

	public static void init() {
		api = Objects.requireNonNull(
						Bukkit.getServicesManager()
								.getRegistration(LuckPerms.class))
								.getProvider();
	}

	public static LuckPerms api() {
		return api;
	}
}
