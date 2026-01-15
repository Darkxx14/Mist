package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.util.dump.DumpUtil;
import com.xyrisdev.mist.util.message.MistMessage;
import net.kyori.adventure.text.event.ClickEvent;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.paper.util.sender.Source;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public class DumpCommand {

	@Command("mist dump")
	@Permission("mist.command.dump")
	public void dump(Source sender) {
		MistMessage.create(sender.source())
				.id("mist_dump_start")
				.send();

		DumpUtil.dump()
				.thenAccept(url ->
						MistMessage.create(sender.source())
								.id("mist_dump_success")
								.placeholder("url", url)
								.interceptor(component ->
										component.clickEvent(ClickEvent.openUrl(url))
								)
								.send()
				)
				.exceptionally(t -> {
					MistMessage.create(sender.source())
							.id("mist_dump_failed")
							.placeholder("error", t.getMessage())
							.send();

					return null;
				});
	}
}
