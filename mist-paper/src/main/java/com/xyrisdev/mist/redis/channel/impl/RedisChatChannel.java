package com.xyrisdev.mist.redis.channel.impl;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.redis.channel.RedisChannel;
import com.xyrisdev.mist.redis.packet.ChatPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

public class RedisChatChannel implements RedisChannel<ChatPacket> {

	private final String serverId;
	private final Function<UUID, ChatUser> userResolver;

	public RedisChatChannel(
			@NotNull String serverId,
			@NotNull Function<UUID, ChatUser> userResolver
	) {
		this.serverId = serverId;
		this.userResolver = userResolver;
	}

	@Override
	public @NotNull String name() {
		return "mist:chat";
	}

	@Override
	public void handle(@NotNull ChatPacket packet) {
		if (packet.server().equals(this.serverId)) {
			return;
		}

		final Component component = GsonComponentSerializer.gson().deserialize(packet.message());

		for (Player viewer : Bukkit.getOnlinePlayers()) {
			final ChatUser user = this.userResolver.apply(viewer.getUniqueId());

			if (user == null) {
				continue;
			}

			if (!user.settings().globalChat()) {
				continue;
			}

			if (user.ignore().contains(packet.sender())) {
				continue;
			}

			viewer.sendMessage(component);
		}
	}
}
