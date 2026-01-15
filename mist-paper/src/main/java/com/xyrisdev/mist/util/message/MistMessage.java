package com.xyrisdev.mist.util.message;

import com.xyrisdev.mist.util.message.builder.MistMessageBuilder;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MistMessage {

	@Contract("_ -> new")
	public static @NotNull MistMessageBuilder create(@NotNull CommandSender sender) {
		return new MistMessageBuilder(sender);
	}

	@Contract("_ -> new")
	public static @NotNull MistMessageBuilder create(@NotNull Audience audience) {
		return new MistMessageBuilder(audience);
	}
}
