package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.ChatPlugin;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerJoinListener {

	public static @NotNull EventHandler<PlayerJoinEvent> listener() {
		return EventBuilder.event(PlayerJoinEvent.class)
				.execute(event -> {
					final UUID id = event.getPlayer().getUniqueId();

					ChatPlugin.service()
							  .userManager()
							  .load(id);
				})
				.build();
	}
}
