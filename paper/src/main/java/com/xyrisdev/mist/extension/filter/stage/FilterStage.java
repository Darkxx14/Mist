package com.xyrisdev.mist.extension.filter.stage;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.filter.alert.AlertPolicy;
import com.xyrisdev.mist.extension.filter.config.FilterConfiguration;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import com.xyrisdev.mist.util.message.builder.MistMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FilterStage implements ChatProcessStage {

	private final FilterConfiguration config;
	private final AlertPolicy alertPolicy;

	public FilterStage(@NotNull FilterConfiguration config) {
		this.config = config;
		this.alertPolicy = new AlertPolicy();
	}

	@Override
	public void process(@NotNull ChatContext ctx) {
		final String original = ctx.plain();

		for (FilterRule rule : config.rules()) {
			final FilterResult result = rule.process(ctx);
			final String ruleKey = rule.name();

			if (result.cancel()) {
				if (alertPolicy.cancel(ruleKey)) {
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
				if (alertPolicy.modify(ruleKey)) {
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
