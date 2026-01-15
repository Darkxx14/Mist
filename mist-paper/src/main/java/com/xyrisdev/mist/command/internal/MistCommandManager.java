package com.xyrisdev.mist.command.internal;

import com.xyrisdev.mist.command.MistCallbackCommand;
import com.xyrisdev.mist.command.MistCommand;
import com.xyrisdev.mist.command.internal.exception.HandledParseException;
import com.xyrisdev.mist.command.internal.argument.AnnouncementArgument;
import com.xyrisdev.mist.command.player.IgnoreCommand;
import com.xyrisdev.mist.command.player.ToggleCommands;
import com.xyrisdev.mist.command.subcommand.*;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.exception.ArgumentParseException;
import org.incendo.cloud.exception.handling.ExceptionHandler;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MistCommandManager {

	private static PaperCommandManager<Source> commandManager;
	private static AnnotationParser<Source> annotationParser;

	public static void of(@NotNull Plugin plugin) {
		if (commandManager != null) {
			throw new IllegalStateException("MistCommandManager already initialized");
		}

		// cmd manager setup
		commandManager = PaperCommandManager
				.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.asyncCoordinator())
				.buildOnEnable(plugin);

		commandManager.exceptionController()
				.registerHandler(
						ArgumentParseException.class,
						ExceptionHandler.unwrappingHandler(HandledParseException.class)
				);

		commandManager.exceptionController()
				.registerHandler(
						HandledParseException.class,
						context -> context.exception().runnable().run()
				);

		annotationParser = new AnnotationParser<>(commandManager, Source.class);

		register(
				// args
				new AnnouncementArgument(),

				// cmds
				new MistCommand(),
				new BroadcastCommand(),
				new ChatCommand(),
				new RegexCommand(),
				new AnnouncementsCommand(),
				new SimilarityCommand(),
				new ReloadCommand(),
				new DumpCommand(),
				new MistCallbackCommand()
		);

		ToggleCommands.register();
		IgnoreCommand.register();
	}

	private static void register(Object @NotNull... containers) {
		Arrays.stream(containers).forEach(container -> annotationParser.parse(container));
	}

	public static @NotNull PaperCommandManager<Source> manager() {
		if (commandManager == null) {
			throw new IllegalStateException("MistCommandManager not initialized yet");
		}

		return commandManager;
	}
}
