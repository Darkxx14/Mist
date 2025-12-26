package com.xyrisdev.mist.listener;

import com.xyrisdev.library.event.builder.EventBuilder;
import com.xyrisdev.library.event.builder.EventHandler;
import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.extension.render.impl.RenderService;
import com.xyrisdev.mist.user.ChatUserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerQuitListener {

	public static @NotNull EventHandler<PlayerQuitEvent> listener() {
		return EventBuilder.event(PlayerQuitEvent.class)
				.execute(event -> {
					final Player player = event.getPlayer();
					final UUID id = event.getPlayer().getUniqueId();

					final ChatUserManager users = ChatPlugin.instance().userManager();

					RenderService.get().invalidate(player);
					users.save(id).thenRun(() -> users.invalidate(id));
				})
				.build();
	}
}
