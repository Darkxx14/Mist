package com.xyrisdev.mist.hook.impl;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.toggle.ChatSettingType;
import com.xyrisdev.mist.hook.MistHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	public static @NotNull MistHook hook() {
		return MistHook.builder()
				.plugin("PlaceholderAPI")
				.onLoad(l -> new PlaceholderAPIHook().register())
				.log(true)
				.success("PlaceholderAPI hooked")
				.failure("PlaceholderAPI not found")
				.build();
	}

	@Override
	public @NotNull String getIdentifier() {
		return ChatPlugin.instance().getPluginMeta().getName();
	}

	@Override
	public @NotNull String getAuthor() {
		return String.join(
				", ",
				ChatPlugin.instance().getPluginMeta().getAuthors()
		);
	}

	@Override
	public @NotNull String getVersion() {
		return ChatPlugin.instance().getPluginMeta().getVersion();
	}

	@Override
	public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
		if (player == null) {
			return null;
		}

		final ChatUser user = ChatPlugin.instance()
				.userManager()
				.get(player.getUniqueId());

		if (user == null) {
			return null;
		}

		return switch (params.toLowerCase()) {
			case "setting_global_chat" ->
					bool(user.settings().enabled(ChatSettingType.GLOBAL_CHAT));

			case "setting_private_messages" ->
					bool(user.settings().enabled(ChatSettingType.PRIVATE_MESSAGES));

			case "setting_mentions" ->
					bool(user.settings().enabled(ChatSettingType.MENTIONS));

			case "setting_announcements" ->
					bool(user.settings().enabled(ChatSettingType.ANNOUNCEMENTS));

			default -> null;
		};
	}

	private static @NotNull String bool(boolean value) {
		return value ? "true" : "false";
	}
}
