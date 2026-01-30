package com.xyrisdev.mist.util.command;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.command.internal.MistCommandManager;
import com.xyrisdev.mist.config.ConfigType;
import lombok.experimental.UtilityClass;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.UnaryOperator;

@UtilityClass
public final class ConfigurableCommand {

	public static void register(@NotNull String key, boolean onlyP, @NotNull UnaryOperator<Command.Builder<Source>> operator) {
		final PaperCommandManager<Source> manager = MistCommandManager.manager();
		final CachableConfiguration config = Mist.INSTANCE.config().get(ConfigType.COMMANDS);

		final String base = "commands." + key;

		if (!config.getBoolean(base + ".enabled", true)) {
			return;
		}

		final String name = config.getString(base + ".base_command");

		if (name == null || name.isBlank()) {
			return;
		}

		final String permission = config.getString(base + ".permission");
		final List<String> aliases = config.getStringList(base + ".aliases");

		Command.Builder<Source> builder = manager.commandBuilder(name, aliases.toArray(String[]::new));

		if (permission != null && !permission.isBlank()) {
			builder = builder.permission(permission);
		}

		if (onlyP) {
			builder = builder.senderType(PlayerSource.class);
		}

		builder = operator.apply(builder);
		manager.command(builder);
	}
}
