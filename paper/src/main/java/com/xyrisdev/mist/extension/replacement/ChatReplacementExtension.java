package com.xyrisdev.mist.extension.replacement;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.extension.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.extension.replacement.config.loader.ReplacementsConfigurationLoader;
import com.xyrisdev.mist.extension.replacement.entry.UnifiedReplacement;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@MistExtension(
		id = "chat_replacement",
		name = "Chat Replacement"
)
@SuppressWarnings("unused")
public class ChatReplacementExtension {

	private final ReplacementsConfiguration config;

	public ChatReplacementExtension() {
		this.config = ReplacementsConfigurationLoader.load(
				Mist.INSTANCE.config().get(ConfigType.CHAT_REPLACEMENTS)
		);
	}

	@ExtensionHandler
	public void handler(@NotNull ChatContext ctx) {
		final Player player = ctx.sender();

		Component message = ctx.message();

		for (UnifiedReplacement replacement : this.config.replacements()) {
			if (!replacement.canApply(player)) {
				continue;
			}

			message = replacement.apply(player, message);
		}

		ctx.message(message);
	}
}
