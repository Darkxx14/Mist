package com.xyrisdev.mist;

import com.google.gson.Gson;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.library.lib.Library;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.command.internal.MistCommandManager;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.config.registry.ConfigRegistry;
import com.xyrisdev.mist.data.DatabaseProvider;
import com.xyrisdev.mist.data.DatabaseProviders;
import com.xyrisdev.mist.extension.ExtensionManager;
import com.xyrisdev.mist.hook.impl.LuckPermsHook;
import com.xyrisdev.mist.hook.impl.PlaceholderAPIHook;
import com.xyrisdev.mist.listener.AsyncChatListener;
import com.xyrisdev.mist.listener.PlayerJoinListener;
import com.xyrisdev.mist.listener.PlayerQuitListener;
import com.xyrisdev.mist.misc.announcement.AnnouncementService;
import com.xyrisdev.mist.redis.LettuceManager;
import com.xyrisdev.mist.redis.channel.impl.RedisChatChannel;
import com.xyrisdev.mist.redis.codec.GsonCodec;
import com.xyrisdev.mist.redis.packet.ChatPacket;
import com.xyrisdev.mist.user.ChatUserManager;
import com.xyrisdev.mist.util.gson.MistGson;
import com.xyrisdev.mist.util.logger.MistLogger;
import com.xyrisdev.mist.util.logger.suppress.FoliaLibSuppressor;
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import com.xyrisdev.mist.util.thread.MistExecutors;
import com.xyrisdev.mist.util.time.DurationParser;
import lombok.Getter;
import lombok.experimental.Accessors;
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

	// redis stuff
	private LettuceManager redis;
	private RedisChatChannel chatChannel;
	private GsonCodec<ChatPacket> chatCodec;

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

		// todo: move ts to its own class
		final CachableConfiguration redis = this.config.get(ConfigType.CONFIGURATION);

		if (redis.getBoolean("redis.enabled", false)) {
			final String uri = redis.getString("redis.uri");
			final String serverId = redis.getString("redis.server-id");

			this.redis = LettuceManager.of(uri);

			this.chatChannel = new RedisChatChannel(
					serverId,
					uuid -> this.userManager.get(uuid)
			);

			this.chatCodec = new GsonCodec<>(
					new Gson(),
					ChatPacket.class
			);

			this.redis.register(this.chatChannel, this.chatCodec);

			MistLogger.info("Redis chat sync enabled (server-id: " + serverId + ")");
		}

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

		if (this.redis != null) {
			this.redis.close();
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
}
