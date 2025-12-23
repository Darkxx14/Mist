package com.xyrisdev.mist.command;

import com.xyrisdev.mist.command.admin.MistCommand;
import com.xyrisdev.mist.command.internal.MistCallbackCommand;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public class MistCommandManager {

	private static PaperCommandManager<Source> MANAGER;

	public static void of(@NotNull Plugin plugin) {
		if (MANAGER != null) {
			throw new IllegalStateException("MistCommandManager already initialized");
		}

		MANAGER = PaperCommandManager
				.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.asyncCoordinator())
				.buildOnEnable(plugin);

		commands();
	}

	public static @NotNull PaperCommandManager<Source> manager() {
		if (MANAGER == null) {
			throw new IllegalStateException("MistCommandManager not initialized yet");
		}

		return MANAGER;
	}

	private static void commands() {
		MistCommand.register();
		MistCallbackCommand.register();
	}
}
