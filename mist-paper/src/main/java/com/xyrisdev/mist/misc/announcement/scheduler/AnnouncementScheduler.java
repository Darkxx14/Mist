package com.xyrisdev.mist.misc.announcement.scheduler;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.xyrisdev.mist.Mist;
import org.jetbrains.annotations.NotNull;

public final class AnnouncementScheduler {

	private WrappedTask task;

	public void start(long intervalMs, @NotNull Runnable action) {
		stop();

		final long ticks = Math.max(1L, intervalMs / 50L);

		this.task = Mist.INSTANCE.scheduler().runTimerAsync(
				action,
				ticks,
				ticks
		);
	}

	public void stop() {
		if (task != null) {
			Mist.INSTANCE.scheduler().cancelTask(task);
			task = null;
		}
	}
}
