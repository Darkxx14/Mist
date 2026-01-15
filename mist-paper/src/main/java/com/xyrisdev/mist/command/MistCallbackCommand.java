package com.xyrisdev.mist.command;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.extension.render.inventory.impl.InventoryRenderService;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
@SuppressWarnings("unused")
public class MistCallbackCommand {

	@Command("mistcallback <id>")
	public void callback(PlayerSource sender, UUID id) {
		final Player player = sender.source();

		Mist.INSTANCE.scheduler()
				.runAtEntity(player, task ->
						InventoryRenderService.get().render(player, id)
				);
	}
}