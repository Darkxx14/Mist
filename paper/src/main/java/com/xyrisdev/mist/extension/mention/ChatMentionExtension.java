package com.xyrisdev.mist.extension.mention;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.extension.mention.config.MentionsConfiguration;
import com.xyrisdev.mist.extension.mention.config.loader.MentionsConfigurationLoader;
import com.xyrisdev.mist.util.message.MistMessage;
import com.xyrisdev.mist.util.text.TextParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.MatchResult;

@MistExtension(
		id = "chat_mentions",
		name = "Chat Mentions"
)
public class ChatMentionExtension {

	private final MentionsConfiguration config;

	public ChatMentionExtension() {
		this.config = MentionsConfigurationLoader.load(
				Mist.INSTANCE.config().get(ConfigType.CHAT_MENTIONS)
		);
	}

	@ExtensionHandler
	public void handler(@NotNull ChatContext ctx) {
		if (!this.config.enabled()) {
			return;
		}

		final Component comp = ctx.message().replaceText(
				TextReplacementConfig.builder()
						.match(this.config.pattern())
						.replacement((match, $$) -> mention(ctx, match))
						.build()
		);

		ctx.message(comp);
	}

	private @NotNull Component mention(@NotNull ChatContext ctx, @NotNull MatchResult match) {
		final String name = match.group(1);
		final Player target = Bukkit.getPlayerExact(name);

		if (target == null) {
			return Component.text(match.group());
		}

		MistMessage.create(target)
				.config(ConfigType.CHAT_MENTIONS)
				.base("mentions")
				.id("notify")
				.placeholder("sender", ctx.sender().getName())
				.send();

		return TextParser.parse(
				ctx.sender(),
				this.config.format().replace("<name>", name)
		);
	}
}
