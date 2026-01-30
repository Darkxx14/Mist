package com.xyrisdev.mist.listener;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.messaging.chat.sync.ChatSyncService;
import com.xyrisdev.mist.user.ChatUserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record IncomingChatListener(
		@NotNull String serverId,
		@NotNull ChatSyncService sync
) {

	public void start() {
		sync.subscribe(packet -> {
			if (packet.getServerId().equals(serverId)) {
				return;
			}

			final UUID senderId = UUID.fromString(packet.getSenderId());
			final Component component = GsonComponentSerializer.gson().deserialize(packet.getMessageJson());
			final ChatUserManager userManager = Mist.INSTANCE.userManager();

			for (Player viewer : Bukkit.getOnlinePlayers()) {
				final ChatUser user = userManager.get(viewer.getUniqueId());

				if (user == null) {
					continue;
				}

				if (user.ignore().contains(senderId)) {
					continue;
				}

				viewer.sendMessage(component);
				Bukkit.getConsoleSender().sendMessage(component);
			}
		});
	}
}
