package com.xyrisdev.mist.misc.announcement;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.misc.announcement.config.AnnouncementConfiguration;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import com.xyrisdev.mist.misc.announcement.scheduler.AnnouncementScheduler;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class AnnouncementService {

	private final AnnouncementScheduler scheduler;
	private AnnouncementConfiguration config;

	public AnnouncementService() {
		this.scheduler = new AnnouncementScheduler();
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

		scheduler.start(config.interval().toMillis(), this::announce);
	}

	public void stop() {
		scheduler.stop();
	}

	private void announce() {
		final Announcement announcement = config.next();

		if (announcement == null) {
			return;
		}

		Bukkit.getOnlinePlayers().stream()
				.filter(player -> {
					final ChatUser user = Mist.INSTANCE.userManager().get(player.getUniqueId());

					return user != null && user.settings().announcements();
				})
				.forEach(player -> send(player, announcement));
	}

	public Optional<Announcement> previewNext() {
		return config == null ? Optional.empty() : config.peekNext();
	}

	public void setNext(@NotNull String name) {
		if (config != null) {
			config.next(name);
		}
	}

	public void force(@NotNull String name) {
		if (config == null) {
			return;
		}

		config.find(name).ifPresent(announcement ->
				Bukkit.getOnlinePlayers().stream()
						.filter(player -> {
							final ChatUser user = Mist.INSTANCE.userManager().get(player.getUniqueId());

							return user != null && user.settings().announcements();
						})
						.forEach(player -> send(player, announcement))
		);
	}

	private void send(@NotNull Player player, @NotNull Announcement announcement) {
		MistMessage.create(player)
				.config(ConfigType.ANNOUNCEMENTS)
				.base("announcements")
				.id(announcement.name())
				.send();
	}

	public Optional<Announcement> find(@NotNull String name) {
		return config == null ? Optional.empty() : config.find(name);
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
