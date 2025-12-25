package com.xyrisdev.mist.misc.announcement.scheduler;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.xyrisdev.mist.ChatPlugin;
import org.jetbrains.annotations.NotNull;

public class AnnouncementScheduler {

	private final ChatPlugin plugin;
	private WrappedTask task;

	public AnnouncementScheduler(@NotNull ChatPlugin plugin) {
		this.plugin = plugin;
	}

	public void start(long intervalMillis, @NotNull Runnable action) {
		stop();

		this.task = plugin.scheduler().runTimerAsync(
				action,
				intervalMillis,
				intervalMillis
		);
	}

	public void stop() {
		if (task != null) {
			plugin.scheduler().cancelTask(task);
			task = null;
		}
	}
}
