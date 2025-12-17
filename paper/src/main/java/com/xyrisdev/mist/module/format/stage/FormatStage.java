package com.xyrisdev.mist.module.format.stage;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.api.processor.ChatStage;
import com.xyrisdev.mist.hook.LuckPermsHook;
import com.xyrisdev.mist.module.format.config.FormatConfiguration;
import com.xyrisdev.mist.module.format.entry.FormatEntry;
import com.xyrisdev.mist.util.text.MistTextParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class FormatStage implements ChatStage {

	private final FormatConfiguration config;

	public FormatStage(@NotNull FormatConfiguration config) {
		this.config = config;
	}

//	@Override
//	@SuppressWarnings("deprecation")
//	public void process(@NotNull ChatContext context) {
//		final Player player = context.player();
//
//		final String group = group(player);
//		final FormatEntry entry = config.resolve(group);
//
//		if (entry == null) {
//			return;
//		}
//
//		final String raw = entry.message().replace("<message>", context.plain());
//
//		Component component = MistTextParser.parse(player, raw);
//
//		// hover
//		if (!entry.hoverText().isEmpty()) {
//			Component hover = Component.empty();
//
//			for (String line : entry.hoverText()) {
//				hover = hover.append(
//						MistTextParser.parse(player, line)
//				).append(Component.newline());
//			}
//
//			component = component.hoverEvent(
//					HoverEvent.showText(hover)
//			);
//		}
//
//		// click
//		if (entry.action() != null) {
//			component = component.clickEvent(
//					ClickEvent.clickEvent(
//							entry.action().action(),
//							entry.action().value()
//					)
//			);
//		}
//
//		context.message(component);
//	}

	@Override
	public void process(@NotNull ChatContext context) {
		final Player player = context.player();

		final String group = group(player);
		final FormatEntry entry = config.resolve(group);

		if (entry == null) {
			return;
		}

		context.format(entry);
	}

	private static @NotNull String group(@NotNull Player player) {
		final LuckPerms luckPerms = LuckPermsHook.api();

		final User user = luckPerms
				.getPlayerAdapter(Player.class)
				.getUser(player);

		return user.getPrimaryGroup();
	}
}
