package com.xyrisdev.mist.misc.announcement.config;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import com.xyrisdev.mist.misc.announcement.object.AnnouncementType;
import com.xyrisdev.mist.util.message.builder.object.MessageType;
import com.xyrisdev.mist.util.time.DurationParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class AnnouncementConfiguration {

	private final boolean enabled;
	private final AnnouncementType type;
	private final Duration interval;
	private final List<Announcement> announcements;

	private int index;
	private Announcement forcedNext;

	@Contract(" -> new")
	public static @NotNull AnnouncementConfiguration load() {
		final CachableConfiguration config = Mist.INSTANCE.config().get(ConfigType.ANNOUNCEMENTS);

		final boolean enabled = config.getBoolean("enabled", false);
		final AnnouncementType type = AnnouncementType.parse(config.getString("type", "random"));
		final Duration interval = DurationParser.parse(config.getString("interval", "5 minutes"));

		final List<Announcement> list = new ArrayList<>();
		final ConfigurationSection root = config.getSection("announcements");

		if (root != null) {
			for (String key : root.getKeys(false)) {
				final ConfigurationSection section = root.getConfigurationSection(key);

				if (section == null) {
					continue;
				}

				final Set<MessageType> types = MessageType.parse(section.getString("type"));

				if (types.isEmpty()) {
					continue;
				}

				list.add(new Announcement(key, types, section));
			}
		}

		return new AnnouncementConfiguration(
				enabled,
				type,
				interval,
				List.copyOf(list)
		);
	}

	public boolean enabled() {
		return enabled && !announcements.isEmpty();
	}

	public Optional<Announcement> peekNext() {
		if (forcedNext != null) {
			return Optional.of(forcedNext);
		}

		if (announcements.isEmpty()) {
			return Optional.empty();
		}

		if (type == AnnouncementType.RANDOM) {
			return Optional.of(
					announcements.get(
							ThreadLocalRandom.current().nextInt(announcements.size())
					)
			);
		}

		return Optional.of(
				announcements.get(
						Math.floorMod(index, announcements.size())
				)
		);
	}

	public Announcement next() {
		if (forcedNext != null) {
			final Announcement a = forcedNext;
			forcedNext = null;
			return a;
		}

		if (type == AnnouncementType.RANDOM) {
			return announcements.get(
					ThreadLocalRandom.current().nextInt(announcements.size())
			);
		}

		return announcements.get(
				Math.floorMod(index++, announcements.size())
		);
	}

	public @NotNull Optional<Announcement> find(@NotNull String name) {
		return announcements.stream()
							.filter(a -> a.name().equalsIgnoreCase(name))
							.findFirst();
	}

	public void next(@NotNull String name) {
		for (int i = 0; i < announcements.size(); i++) {
			if (announcements.get(i).name().equalsIgnoreCase(name)) {
				this.index = i;
				return;
			}
		}
	}
}
