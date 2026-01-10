package com.xyrisdev.mist.extension.filter;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.extension.filter.alert.AlertRules;
import com.xyrisdev.mist.extension.filter.config.FilterConfiguration;
import com.xyrisdev.mist.extension.filter.config.loader.FilterConfigurationLoader;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import com.xyrisdev.mist.util.message.builder.MistMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MistExtension(
		id = "chat_filter",
		name = "Chat Filter"
)
public class ChatFilterExtension {

	private final FilterConfiguration config;
	private final AlertRules alerts;

	public ChatFilterExtension() {
		this.config = FilterConfigurationLoader.load(
				Mist.INSTANCE.config().get(ConfigType.CHAT_FILTER)
		);

		this.alerts = new AlertRules();
	}

	@ExtensionHandler
	public void handler(@NotNull ChatContext ctx) {
		final String original = ctx.plain();

		for (FilterRule rule : config.rules()) {
			final FilterResult result = rule.process(ctx);
			final String ruleKey = rule.name();

			if (result.cancel()) {
				if (alerts.cancel(ruleKey)) {
					alert(
							"alert_cancelled",
							ctx.sender(),
							original,
							null,
							ruleKey,
							rule.displayName()
					);
				}

				ctx.cancel();
				return;
			}

			if (result.modifiedMessage() != null) {
				if (alerts.modify(ruleKey)) {
					alert(
							"alert_modified",
							ctx.sender(),
							original,
							result.modifiedMessage(),
							ruleKey,
							rule.displayName()
					);
				}

				ctx.plain(result.modifiedMessage());
			}
		}
	}

	private void alert(
			@NotNull String messageId, @NotNull Player player,
			@NotNull String message, @Nullable String modifiedMessage,
			@NotNull String rule, @NotNull String displayName
	) {
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (!staff.hasPermission("mist.admin")) {
				continue;
			}

			final MistMessageBuilder msg = MistMessage.create(staff)
					.id(messageId)
					.placeholder("player", player.getName())
					.placeholder("message", message)
					.placeholder("rule", rule)
					.placeholder("rule_display", displayName);

			if (modifiedMessage != null) {
				msg.placeholder("modified_message", modifiedMessage);
			}

			msg.send();
		}
	}
}
