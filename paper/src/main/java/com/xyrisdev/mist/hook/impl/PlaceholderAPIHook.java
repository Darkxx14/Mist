package com.xyrisdev.mist.hook.impl;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.toggle.ChatSettingType;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.hook.MistHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	public static @NotNull MistHook hook() {
		return MistHook.builder()
				.plugin("PlaceholderAPI")
				.onLoad(l -> new PlaceholderAPIHook().register())
				.log(true)
				.success("Hooked into PlaceholderAPI")
				.failure("PlaceholderAPI not found")
				.build();
	}

	@Override
	public @NotNull String getIdentifier() {
		return Mist.INSTANCE.plugin().getPluginMeta().getName();
	}

	@Override
	public @NotNull String getAuthor() {
		return String.join(
				", ",
				Mist.INSTANCE.plugin().getPluginMeta().getAuthors()
		);
	}

	@Override
	public @NotNull String getVersion() {
		return Mist.INSTANCE.plugin().getPluginMeta().getVersion();
	}

	@Override
	public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
		if (player == null) {
			return null;
		}

		final ChatUser user = Mist.INSTANCE.userManager().get(player.getUniqueId());

		if (user == null) {
			return null;
		}

		return switch (params.toLowerCase()) {
			case "setting_global_chat" -> bool(
					"mist_setting_global_chat",
					user.settings().enabled(ChatSettingType.GLOBAL_CHAT)
			);

			case "setting_private_messages" -> bool(
					"mist_setting_private_messages",
					user.settings().enabled(ChatSettingType.PRIVATE_MESSAGES)
			);

			case "setting_mentions" -> bool(
					"mist_setting_mentions",
					user.settings().enabled(ChatSettingType.MENTIONS)
			);

			case "setting_announcements" -> bool(
					"mist_setting_announcements",
					user.settings().enabled(ChatSettingType.ANNOUNCEMENTS)
			);

			default -> null;
		};
	}

	// internal utils
	private @NotNull String bool(@NotNull String placeholder, boolean value) {
		final ConfigurationSection section = Mist.INSTANCE.config()
											.get(ConfigType.PLACEHOLDERS)
											.getSection("placeholders");

		if (section == null) {
			return value ? "enabled" : "disabled";
		}

		final ConfigurationSection section1 = section.getConfigurationSection(placeholder);

		if (section1 == null) {
			return value ? "enabled" : "disabled";
		}

		return section1.getString(
				value ? "true" : "false",
				value ? "enabled" : "disabled"
		);
	}
}