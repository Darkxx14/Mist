package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.module.render.impl.RenderService;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener {

	public static @NotNull EventHandler<PlayerQuitEvent> listener() {
		return EventBuilder.event(PlayerQuitEvent.class)
				.execute(event -> {
					final Player player = event.getPlayer();

					RenderService.get().invalidate(player);
				})
				.build();
	}
}
