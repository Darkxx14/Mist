package com.xyrisdev.mist.loader;

import com.xyrisdev.mist.command.admin.MistCommand;
import com.xyrisdev.mist.command.internal.MistCallbackCommand;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public final class MistPaperBootstrapper implements PluginBootstrap {

	@Override
	public void bootstrap(@NotNull BootstrapContext context) {
		context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			commands.registrar().register(MistCommand.create());
			commands.registrar().register(MistCallbackCommand.create());
		});
	}

	@Override
	public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
		return PluginBootstrap.super.createPlugin(context);
	}
}