package com.xyrisdev.mist.misc.announcement;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.misc.announcement.config.AnnouncementConfiguration;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import com.xyrisdev.mist.misc.announcement.scheduler.AnnouncementScheduler;
import com.xyrisdev.mist.util.message.builder.object.MessageContext;
import com.xyrisdev.mist.util.message.builder.object.MessageType;
import com.xyrisdev.mist.util.message.effect.SoundEffect;
import com.xyrisdev.mist.util.message.render.ActionBarRenderer;
import com.xyrisdev.mist.util.message.render.ChatRenderer;
import com.xyrisdev.mist.util.message.render.TitleRenderer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnnouncementService {

	private final AnnouncementScheduler scheduler;
	private AnnouncementConfiguration config;

	public AnnouncementService(@NotNull ChatPlugin plugin) {
		this.scheduler = new AnnouncementScheduler(plugin);
	}

	public void start() {
		reload();
	}

	public void reload() {
		scheduler.stop();

		this.config = AnnouncementConfiguration.load();

		if (!config.enabled()) {
			return;
		}

		scheduler.start(
				config.interval().toMillis(),
				this::announceOnce
		);
	}

	public void stop() {
		scheduler.stop();
	}

	private void announceOnce() {
		final Announcement announcement = config.next();
		if (announcement == null) {
			return;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			send(player, announcement);
		}
	}

	public Optional<Announcement> nextPreview() {
		return config == null ? Optional.empty() : config.peekNext();
	}

	public void setNext(@NotNull String name) {
		if (config != null) {
			config.next(name);
		}
	}

	public void forceNow(@NotNull String name) {
		if (config == null) {
			return;
		}

		config.find(name).ifPresent(announcement -> {
			for (Player online : Bukkit.getOnlinePlayers()) {
				send(online, announcement);
			}
		});
	}

	private void send(@NotNull Player player, @NotNull Announcement announcement) {
		final EnumSet<MessageType> types = announcement.type();
		if (types.isEmpty()) {
			return;
		}

		final ConfigurationSection section = announcement.section();
		final MessageContext ctx = new MessageContext(Map.of());

		if (types.contains(MessageType.CHAT)) {
			ChatRenderer.render(player, player, section, ctx);
		}

		if (types.contains(MessageType.ACTION_BAR)) {
			ActionBarRenderer.render(player, section, ctx);
		}

		if (types.contains(MessageType.TITLE)) {
			TitleRenderer.render(player, section, ctx);
		}

		final ConfigurationSection sound = section.getConfigurationSection("sound");

		if (sound != null) {
			SoundEffect.play(
					player,
					sound
			);
		}
	}

	public Optional<Announcement> find(@NotNull String name) {
		return config == null
				? Optional.empty()
				: config.find(name);
	}

	public List<String> announcementNames() {
		if (config == null) {
			return List.of();
		}

		return config.announcements().stream()
				.map(Announcement::name)
				.toList();
	}
}
