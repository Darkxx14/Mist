package com.xyrisdev.mist.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public abstract class AbstractHook {

	private boolean running;
	private Logger logger;

	protected abstract @NotNull String id();
	protected abstract boolean required();

	protected abstract void run() throws Exception;

	protected void shutdown() {}

	final void start(@NotNull Plugin plugin) {
		if (running) {
			return;
		}

		logger = plugin.getLogger();

		if (!Bukkit.getPluginManager().isPluginEnabled(id())) {
			logger.warning("Hook not found: " + id());

			if (required()) {
				logger.severe("Required hook missing: " + id());
				Bukkit.getPluginManager().disablePlugin(plugin);
			}
			return;
		}

		try {
			run();
			running = true;
			logger.info("Hook loaded: " + id());
		} catch (Throwable t) {
			logger.severe("Hook failed: " + id());
			t.printStackTrace();

			if (required()) {
				Bukkit.getPluginManager().disablePlugin(plugin);
			}
		}
	}

	final void stop() {
		if (!running) {
			return;
		}

		shutdown();
		running = false;

		if (logger != null) {
			logger.info("Hook unloaded: " + id());
		}
	}

	public final boolean running() {
		return running;
	}
}
