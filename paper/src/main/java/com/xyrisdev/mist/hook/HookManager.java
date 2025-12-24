package com.xyrisdev.mist.hook;

import com.xyrisdev.mist.hook.impl.LuckPermsHook;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HookManager {

	private static final HookManager INSTANCE = new HookManager();

	private final List<AbstractHook> hooks = List.of(
			new LuckPermsHook()
	);

	public static HookManager of() {
		return INSTANCE;
	}

	public void load(Plugin plugin) {
		hooks.forEach(hook -> hook.start(plugin));
	}

	public void unload() {
		hooks.forEach(AbstractHook::stop);
	}
}
