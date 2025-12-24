package com.xyrisdev.mist.hook.impl;

import com.xyrisdev.mist.hook.AbstractHook;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LuckPermsHook extends AbstractHook {

	private static LuckPerms luckPerms;

	@Contract(pure = true)
	@Override
	protected @NotNull String id() {
		return "LuckPerms";
	}

	@Override
	protected boolean required() {
		return true;
	}

	@Override
	protected void run() {
		luckPerms = Objects.requireNonNull(Bukkit.getServicesManager()
						.getRegistration(LuckPerms.class))
						.getProvider();
	}

	@Override
	protected void shutdown() {
		luckPerms = null;
	}

	public static LuckPerms luckPerms() {
		return luckPerms;
	}
}
