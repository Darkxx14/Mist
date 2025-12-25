package com.xyrisdev.mist.misc.announcement.object;

import com.xyrisdev.mist.util.message.builder.object.MessageType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public record Announcement(
		@NotNull EnumSet<MessageType> type,
		@NotNull ConfigurationSection section
) {}
