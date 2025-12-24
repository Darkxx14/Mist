package com.xyrisdev.mist.command.admin.subcommand;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.command.admin.parser.ConfigPathParser;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.leangen.geantyref.TypeToken;
import org.incendo.cloud.parser.ParserDescriptor;

import java.util.Optional;

public class ReloadCommand {

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(
				root.literal("reload")
						.optional("file", configPathDescriptor())
						.permission("mist.commands.reload")
						.handler(this::reload)
		);
	}

	private void reload(@NotNull CommandContext<Source> ctx) {
		final long start = System.currentTimeMillis();

		final Optional<String> file = ctx.optional("file");

		if (file.isPresent()) {
			MistPaperPlugin.instance()
					.configRegistry()
					.reload(ConfigType.valueOf(file.get()));
		} else {
			MistPaperPlugin.instance().reload();
		}

		MistMessage.create(ctx.sender().source())
				.id("mist_reloaded")
				.placeholder(
						"time_taken",
						String.valueOf(System.currentTimeMillis() - start)
				)
				.send();
	}

	@Contract(" -> new")
	private @NotNull ParserDescriptor<Source, String> configPathDescriptor() {
		return ParserDescriptor.of(
				new ConfigPathParser<>(
						MistPaperPlugin.instance().configRegistry()
				),
				TypeToken.get(String.class)
		);
	}
}
