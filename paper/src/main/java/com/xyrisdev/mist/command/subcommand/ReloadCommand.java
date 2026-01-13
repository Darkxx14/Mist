package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.paper.util.sender.Source;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public class ReloadCommand {

	@Command("mist reload")
	@Permission("mist.command.reload")
	public void reload(Source sender) {
		final long start = System.currentTimeMillis();

		Mist.INSTANCE.reload();

		MistMessage.create(sender.source())
				.id("mist_reloaded")
				.placeholder(
						"time_taken",
						String.valueOf(System.currentTimeMillis() - start)
				)
				.send();
	}
}
