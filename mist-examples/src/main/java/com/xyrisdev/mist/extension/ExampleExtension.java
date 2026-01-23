package com.xyrisdev.mist.extension;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import org.jetbrains.annotations.NotNull;

@MistExtension(
		id = "my_extension",
		name = "this is my extension"
)
@SuppressWarnings("unused")
public final class ExampleExtension {

	// plugins we dont want mentioned
	private static final String[] plugins = {
			"interactivechat",
			"zelchat",
			"chatcontrolred",
			"redischat",
			"tchat"
	};

	@ExtensionHandler
	public void handler(@NotNull ChatContext ctx) {
		final String message = ctx.plain().toLowerCase();

		// loop
		for (String plugin : plugins) {
			if (!message.contains(plugin)) {
				continue;
			}

			// nope, not allowed
			ctx.cancel();
			ctx.sender().sendRichMessage("<red>Mist is best <3");
			return;
		}
	}
}
