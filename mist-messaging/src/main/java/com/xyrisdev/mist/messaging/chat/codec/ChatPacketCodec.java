package com.xyrisdev.mist.messaging.chat.codec;

import com.xyrisdev.mist.messaging.chat.packet.ChatPacket;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ChatPacketCodec {

	public byte[] encode(@NotNull String server, @NotNull UUID sender, @NotNull String msg) {
		return ChatPacket.newBuilder()
				.setServerId(server)
				.setSenderId(sender.toString())
				.setMessageJson(msg)
				.build()
				.toByteArray();
	}

	public ChatPacket decode(byte @NotNull [] payload) {
		try {
			return ChatPacket.parseFrom(payload);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to decode ChatPacket", e);
		}
	}
}
