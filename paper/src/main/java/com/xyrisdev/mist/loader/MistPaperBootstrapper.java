package com.xyrisdev.mist.loader;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class MistPaperBootstrapper implements PluginBootstrap {

	@Override
	public void bootstrap(@NotNull BootstrapContext ctx) {}

	@Override
	public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext ctx) {
		return PluginBootstrap.super.createPlugin(ctx);
	}
}