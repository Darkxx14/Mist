package com.xyrisdev.mist.extension.format;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.extension.format.config.loader.FormatConfigurationLoader;
import com.xyrisdev.mist.hook.impl.LuckPermsHook;
import com.xyrisdev.mist.extension.format.config.FormatConfiguration;
import com.xyrisdev.mist.extension.format.entry.FormatEntry;
import net.kyori.adventure.key.Key;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@MistExtension(
		id = "chat_format",
		name = "Chat Format"
)
@SuppressWarnings("unused")
public class ChatFormatExtension {

	private final FormatConfiguration config;

	public ChatFormatExtension() {
		this.config = FormatConfigurationLoader.load(
				Mist.INSTANCE.config().get(ConfigType.CHAT_FORMAT)
		);
	}

	@ExtensionHandler
	public void handler(@NotNull ChatContext ctx) {
		final Player player = ctx.sender();

		final String group = group(player);
		final FormatEntry entry = this.config.resolve(group);

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
