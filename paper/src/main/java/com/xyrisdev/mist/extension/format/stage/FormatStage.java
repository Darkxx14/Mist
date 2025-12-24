package com.xyrisdev.mist.extension.format.stage;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.hook.impl.LuckPermsHook;
import com.xyrisdev.mist.extension.format.config.FormatConfiguration;
import com.xyrisdev.mist.extension.format.entry.FormatEntry;
import net.kyori.adventure.key.Key;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FormatStage implements ChatProcessStage {

	private final FormatConfiguration config;

	public FormatStage(@NotNull FormatConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext ctx) {
		final Player player = ctx.sender();

		final String group = group(player);
		final FormatEntry entry = config.resolve(group);

		if (entry == null) {
			return;
		}

		ctx.attribute(Key.key("mist", "render"), entry);
	}

	private static @NotNull String group(@NotNull Player player) {
		final LuckPerms luckPerms = LuckPermsHook.luckPerms();

		final User user = luckPerms
				.getPlayerAdapter(Player.class)
				.getUser(player);

		return user.getPrimaryGroup();
	}
}
