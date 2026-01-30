package com.xyrisdev.mist;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.xyrisdev.library.lib.Library;
import com.xyrisdev.mist.adapter.nats.NATSMessagingBus;
import com.xyrisdev.mist.adapter.redis.RedisMessagingBus;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.api.messaging.MessagingBus;
import com.xyrisdev.mist.command.internal.MistCommandManager;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.config.registry.ConfigRegistry;
import com.xyrisdev.mist.data.DatabaseProvider;
import com.xyrisdev.mist.data.DatabaseProviders;
import com.xyrisdev.mist.extension.ExtensionManager;
import com.xyrisdev.mist.hook.impl.LuckPermsHook;
import com.xyrisdev.mist.hook.impl.PlaceholderAPIHook;
import com.xyrisdev.mist.listener.AsyncChatListener;
import com.xyrisdev.mist.listener.IncomingChatListener;
import com.xyrisdev.mist.listener.PlayerJoinListener;
import com.xyrisdev.mist.listener.PlayerQuitListener;
import com.xyrisdev.mist.messaging.chat.sync.ChatSyncService;
import com.xyrisdev.mist.misc.announcement.AnnouncementService;
import com.xyrisdev.mist.user.ChatUserManager;
import com.xyrisdev.mist.util.logger.MistLogger;
import com.xyrisdev.mist.util.logger.suppress.FoliaLibSuppressor;
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import com.xyrisdev.mist.util.thread.MistExecutors;
import com.xyrisdev.mist.util.time.DurationParser;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@SuppressWarnings("java:S6548")
@Getter
@Accessors(fluent = true)
public enum Mist {

	INSTANCE;

	private MistPlugin plugin;

	private ConfigRegistry config;
	private FoliaLib folia;
	private PlatformScheduler scheduler;
	private DatabaseProvider database;
	private ChatUserManager userManager;
	private ChatProcessor chatProcessor;
	private AnnouncementService announcements;

	// sync stuff
	private MessagingBus messagingBus;
	private ChatSyncService sync;

	public static void load(@NotNull MistPlugin plugin) {
		INSTANCE.plugin = plugin;
	}

	public void start() {
		if (this.plugin == null) {
			throw new IllegalStateException("Mist was not loaded. Call Mist.load(plugin) before start().");
		}

		Library.of(this.plugin, "mist-paper");

		MistLogger.startup(
				this.plugin.getPluginMeta().getVersion(),
				this.plugin.server()
		);

		this.config = ConfigRegistry.load();

		this.folia = FoliaLibSuppressor.suppress(this.plugin, () -> new FoliaLib(this.plugin));
		this.scheduler = this.folia.getScheduler();

		MistExecutors.start();

		this.database = DatabaseProviders.create(config.get(ConfigType.CONFIGURATION));
		this.database.connect();

		this.userManager = new ChatUserManager(database.users());

		this.autoSave();

		this.chatProcessor = ExtensionManager.register();
		this.chatSync();

		LeetMap.load(this.config);
		RegexGenerator.index();

		LuckPermsHook.hook().register();
		PlaceholderAPIHook.hook().register();

		this.announcements = new AnnouncementService();
		this.announcements.start();

		AsyncChatListener.listener().register();
		PlayerJoinListener.listener().register();
		PlayerQuitListener.listener().register();

		MistCommandManager.of(this.plugin);
	}

	public void shutdown() {
		if (this.announcements != null) {
			this.announcements.stop();
		}

		if (this.userManager != null) {
			this.userManager.flush();
			this.userManager.invalidateAll();
		}

		if (this.database != null) {
			this.database.shutdown();
		}

		if (this.scheduler != null) {
			this.scheduler.cancelAllTasks();
		}

		if (this.messagingBus != null) {
			try {
				this.messagingBus.close();
			} catch (Exception exc) {
				MistLogger.warn("failed to shutdown messaging bus: " + exc.getMessage());
			}
		}

		MistExecutors.shutdown();
	}

	public void reload() {
		this.config.reloadAll();

		this.chatProcessor = ExtensionManager.register();

		LeetMap.load(this.config);
		RegexGenerator.index();

		if (this.announcements != null) {
			this.announcements.reload();
		}
	}

	// todo maybe move this thing somehwere better
	private void autoSave() {
		final Duration duration = DurationParser.parse(
				this.config
						.get(ConfigType.CONFIGURATION)
						.getString("data.auto_save_interval", "10 minutes")
		);

		if (duration.isZero() || duration.isNegative()) {
			return;
		}

		final long ticks = DurationParser.toTicks(duration);

		this.scheduler.runTimerAsync(
				this.userManager::flush,
				ticks,
				ticks
		);
	}

	// todo maybe move this thing somehwere better
	private void chatSync() {
		final ConfigurationSection section = this.config.get(ConfigType.CONFIGURATION).getSection("chat_synchronization");

		if (section == null || !section.getBoolean("enabled", false)) {
			return;
		}

		final String serverId = section.getString("server_id", "server1");
		final String adapter = section.getString("adapter", "redis").toLowerCase();

		this.messagingBus = switch (adapter) {
			case "redis" -> {
				final String uri = section.getString("redis.uri");

				if (uri == null) {
					throw new IllegalStateException("redis adapter selected but redis.uri is missing");
				}

				yield new RedisMessagingBus(uri);
			}

			case "nats" -> {
				final String uri = section.getString("nats.uri");

				if (uri == null) {
					throw new IllegalStateException("NATS adapter selected but nats.uri is missing");
				}

				yield new NATSMessagingBus(uri);
			}

			default -> throw new IllegalStateException("unknown chat synchronization adapter: " + adapter);
		};

		this.sync = new ChatSyncService(this.messagingBus);
		new IncomingChatListener(serverId, this.sync).start();

		MistLogger.info("Chat synchronization enabled (adapter=%s, server id=%s)".formatted(adapter, serverId));
	}
}