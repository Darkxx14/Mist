package com.xyrisdev.mist.extension.render.common;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.extension.render.command.CommandRenderHandler;
import com.xyrisdev.mist.extension.render.config.RenderConfiguration;
import com.xyrisdev.mist.extension.render.config.loader.RenderConfigurationLoader;
import com.xyrisdev.mist.extension.render.inventory.InventoryRenderHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@MistExtension(
		id = "render",
		name = "Render"
)
public class RenderExtension {

	private final List<RenderHandler> handlers;

	public RenderExtension() {
		final RenderConfiguration config = RenderConfigurationLoader.load(
				Mist.INSTANCE.config().get(ConfigType.RENDER)
		);

		this.handlers = List.of(
				new InventoryRenderHandler(config),
				new CommandRenderHandler(config)
		);
	}

	@ExtensionHandler
	public void handler(@NotNull ChatContext ctx) {
		for (RenderHandler handler : handlers) {
			handler.handle(ctx);
		}
	}
}