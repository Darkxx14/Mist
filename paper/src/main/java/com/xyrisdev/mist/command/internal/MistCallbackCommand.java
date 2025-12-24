package com.xyrisdev.mist.command.internal;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.command.MistCommandManager;
import com.xyrisdev.mist.extension.render.impl.RenderService;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.UUIDParser;

import java.util.UUID;

public class MistCallbackCommand {

	public static void register() {
		final PaperCommandManager<Source> manager = MistCommandManager.manager();

		manager.command(
				manager.commandBuilder("mistcallback")
						.senderType(PlayerSource.class)
						.required("id", UUIDParser.uuidParser())
						.handler(ctx -> {
							final Player player = ctx.sender().source();
							final UUID id = ctx.get("id");

							MistPaperPlugin.instance()
									.scheduler()
									.runTask(player, () -> RenderService.get().render(player, id));
						})
		);
	}
}
