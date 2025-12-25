package com.xyrisdev.mist.util.message.builder;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.message.builder.object.MessageContext;
import com.xyrisdev.mist.util.message.builder.object.MessageType;
import com.xyrisdev.mist.util.message.effect.SoundEffect;
import com.xyrisdev.mist.util.message.render.ActionBarRenderer;
import com.xyrisdev.mist.util.message.render.ChatRenderer;
import com.xyrisdev.mist.util.message.render.TitleRenderer;
import com.xyrisdev.mist.util.text.TextParser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class MistMessageBuilder {

	private final Audience audience;
	private final Player player;

	private final Map<String, String> placeholders = new HashMap<>();
	private String id;

	private UnaryOperator<Component> interceptor;

	private ConfigType configType = ConfigType.LANGUAGE;
	private String basePath = "messages";

	public MistMessageBuilder(@NotNull CommandSender sender) {
		this.audience = sender;
		this.player = sender instanceof Player p ? p : null;
	}

	public MistMessageBuilder(@NotNull Audience audience) {
		this.audience = audience;
		this.player = audience instanceof Player p ? p : null;
	}

	public @NotNull MistMessageBuilder id(@NotNull String id) {
		this.id = id;
		return this;
	}

	public @NotNull MistMessageBuilder placeholder(@NotNull String key, @NotNull String value) {
		placeholders.put(key, value);
		return this;
	}

	public @NotNull MistMessageBuilder config(@NotNull ConfigType type) {
		this.configType = type;
		return this;
	}

	public @NotNull MistMessageBuilder base(@NotNull String basePath) {
		this.basePath = basePath;
		return this;
	}

	public @NotNull MistMessageBuilder interceptor(@NotNull UnaryOperator<Component> interceptor) {
		this.interceptor = interceptor;
		return this;
	}

	public void send() {
		Objects.requireNonNull(id, "Message id must be set");

		final ConfigurationSection section = ChatPlugin.instance()
				.configRegistry()
				.get(configType)
				.getSection(basePath + "." + id);

		if (section == null) {
			return;
		}

		final EnumSet<MessageType> types = MessageType.parse(section.getString("type"));

		if (types.isEmpty()) {
			return;
		}

		final String prefix = ChatPlugin.instance()
				.configRegistry()
				.get(ConfigType.LANGUAGE)
				.getString("prefix", "");

		final MessageContext ctx = new MessageContext(placeholders);

		ctx.interceptor(this.interceptor);
		ctx.component("prefix", TextParser.parse(audience, prefix));

		if (types.contains(MessageType.CHAT)) {
			final ConfigurationSection chat = section.getConfigurationSection("chat");

			if (chat != null) {
				ChatRenderer.render(audience, player, chat, ctx);
			}
		}

		if (player != null && types.contains(MessageType.ACTION_BAR)) {
			final ConfigurationSection bar = section.getConfigurationSection("action_bar");

			if (bar != null) {
				ActionBarRenderer.render(player, bar, ctx);
			}
		}

		if (player != null && types.contains(MessageType.TITLE)) {
			final ConfigurationSection title = section.getConfigurationSection("title");

			if (title != null) {
				TitleRenderer.render(player, title, ctx);
			}
		}

		final ConfigurationSection sound = section.getConfigurationSection("sound");

		if (player != null && sound != null) {
			SoundEffect.play(
					player,
					sound
			);
		}
	}
}
