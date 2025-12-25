package com.xyrisdev.mist.command.admin;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.command.MistCommandManager;
import com.xyrisdev.mist.command.admin.subcommand.*;
import com.xyrisdev.mist.util.build.BuildInformation;
import com.xyrisdev.mist.util.message.MistMessage;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public class MistCommand {

	public static void register() {
		final PaperCommandManager<Source> manager = MistCommandManager.manager();

		final Command.Builder<Source> root = manager.commandBuilder("mist")
				.permission("mist.command")
				.handler(MistCommand::about);

		manager.command(root);

		new ReloadCommand().register(manager, root);
		new BroadcastCommand().register(manager, root);
		new SimilarityCommand().register(manager, root);
		new ChatCommand().register(manager, root);
		new RegexCommand().register(manager, root);
		new AboutCommand().register(manager, root);
		new AnnouncementsCommand().register(manager, root);
	}

	public static void about(@NotNull CommandContext<Source> ctx) {
		final BuildInformation build = BuildInformation.instance();
		final PluginMeta meta = ChatPlugin.instance().getPluginMeta();

		MistMessage.create(ctx.sender().source())
				.id("mist_about")
				.placeholder("authors", String.join(", ", meta.getAuthors()))
				.placeholder("module", build.module())
				.placeholder("version", build.version())
				.placeholder("branch", build.branch())
				.placeholder("commit", build.commit())
				.placeholder("commit_short", build.commitShort())
				.interceptor(component ->
						component
								.hoverEvent(HoverEvent.showText(
										Component.text("Click to star on GitHub").color(NamedTextColor.GRAY)
								))
								.clickEvent(ClickEvent.openUrl("https://github.com/Darkxx14/Mist"))
				)
				.send();
	}
}