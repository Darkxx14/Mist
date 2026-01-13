package com.xyrisdev.mist.command;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.util.build.BuildInformation;
import com.xyrisdev.mist.util.message.MistMessage;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MistCommand {

	@Command("mist")
	@Permission("mist.command")
	public void root(Source sender) {
		this.about(sender);
	}

	@Command("mist about")
	@Permission("mist.command.about")
	public void about(@NotNull Source sender) {
		final BuildInformation build = BuildInformation.instance();
		final PluginMeta meta = Mist.INSTANCE.plugin().getPluginMeta();

		MistMessage.create(sender.source())
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
										Component.text("Click to star on GitHub")
												.color(NamedTextColor.GRAY)
								))
								.clickEvent(ClickEvent.openUrl(
										"https://github.com/Darkxx14/Mist"
								))
				)
				.send();
	}
}
