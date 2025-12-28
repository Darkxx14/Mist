package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.util.dump.DumpUtil;
import com.xyrisdev.mist.util.message.MistMessage;
import net.kyori.adventure.text.event.ClickEvent;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public class DumpCommand {

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(
				root.literal("dump")
						.permission("mist.command.dump")
						.handler(this::dump)
		);
	}

	private void dump(@NotNull CommandContext<Source> ctx) {
		MistMessage.create(ctx.sender().source())
				.id("mist_dump_start")
				.send();

		DumpUtil.dump()
				.thenAccept(url ->
						MistMessage.create(ctx.sender().source())
								.id("mist_dump_success")
								.placeholder("url", url)
								.interceptor(component ->
										component.clickEvent(ClickEvent.openUrl(url))
								)
								.send()
				)
				.exceptionally(t -> {
					MistMessage.create(ctx.sender().source())
							.id("mist_dump_failed")
							.placeholder("error", t.getMessage())
							.send();
					return null;
				});
	}
}
