package com.xyrisdev.mist.misc.announcement.object;

import com.xyrisdev.mist.util.message.builder.object.MessageType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record Announcement(
		@NotNull String name,
		@NotNull Set<MessageType> type,
		@NotNull ConfigurationSection section
) {}
