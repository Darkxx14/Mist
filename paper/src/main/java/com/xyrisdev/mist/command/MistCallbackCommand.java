package com.xyrisdev.mist.command;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.extension.render.inventory.impl.InventoryRenderService;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
public class MistCallbackCommand {

	@Command("mistcallback <id>")
	public void callback(@NotNull PlayerSource sender, @Argument("id") UUID id) {
		final Player player = sender.source();

		Mist.INSTANCE.scheduler()
				.runAtEntity(player, task ->
						InventoryRenderService.get().render(player, id)
				);
	}
}