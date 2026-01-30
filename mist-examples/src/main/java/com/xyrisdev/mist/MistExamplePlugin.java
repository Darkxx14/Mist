package com.xyrisdev.mist;

import com.xyrisdev.mist.api.MistAPI;
import com.xyrisdev.mist.api.chat.extension.registry.MistExtensionRegistry;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.event.MistEventBus;
import com.xyrisdev.mist.api.event.user.ChatUserLoadEvent;
import com.xyrisdev.mist.api.event.user.ChatUserSaveEvent;
import com.xyrisdev.mist.extension.ExampleExtension;
import org.bukkit.plugin.java.JavaPlugin;

public final class MistExamplePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		// access the extension registry
		final MistExtensionRegistry registry = MistAPI.extensions();

		// access the event bus
		final MistEventBus eventBus = MistAPI.eventBus();

		/*
		 * registers our custom chat extension
		 *
		 * execution order is controlled via:
		 * plugins/Mist/internal/extension_priorities.yml
		 *
		 * use the extension id ("my_extension") as the yaml key.
		 * lower numbers run earlier.
		 */
		registry.register(new ExampleExtension());

		// listen for chat user load event
		eventBus.on(ChatUserLoadEvent.class)
				.run(event -> {
					final ChatUser user = event.user();
					this.getLogger().info("User -> Loaded %s".formatted(user.id()));
				});

		// listen for chat user save event
		eventBus.on(ChatUserSaveEvent.class)
				.run(event -> {
					final ChatUser user = event.user();
					this.getLogger().info("User -> Saved %s to the database".formatted(user.id()));
				});
	}
}
