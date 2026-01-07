package com.xyrisdev.mist.misc.announcement.scheduler;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.xyrisdev.mist.ChatPlugin;
import org.jetbrains.annotations.NotNull;

public class AnnouncementScheduler {

	private WrappedTask task;

	public void start(long intervalMs, @NotNull Runnable action) {
		stop();

		final long ticks = Math.max(1L, intervalMs / 50L);

		this.task = ChatPlugin.service().scheduler().runTimerAsync(
				action,
				ticks,
				ticks
		);
	}

	public void stop() {
		if (task != null) {
			ChatPlugin.service().scheduler().cancelTask(task);
			task = null;
		}
	}
}
