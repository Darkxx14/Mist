package com.xyrisdev.mist;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
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
import com.xyrisdev.mist.user.ChatUserManager;
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

@Getter
@Accessors(fluent = true)
public final class MistService {

    private final ChatPlugin plugin;

    private ConfigRegistry configRegistry;
    private FoliaLib folia;
    private PlatformScheduler scheduler;
    private DatabaseProvider database;
    private ChatUserManager userManager;
    private ChatProcessor chatProcessor;
    private AnnouncementService announcements;

    public MistService(@NotNull ChatPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Library.of(this.plugin, "mist-paper");

        MistLogger.startup(
                this.plugin.getPluginMeta().getVersion(),
                this.plugin.server()
        );

        this.configRegistry = ConfigRegistry.load();
        this.folia = FoliaLibSuppressor.suppress(this.plugin, () -> new FoliaLib(this.plugin));
        this.scheduler = this.folia.getScheduler();

        MistExecutors.start();

        this.database = DatabaseProviders.create(configRegistry.get(ConfigType.CONFIGURATION));
        this.database.connect();

        this.userManager = new ChatUserManager(database.users());

        final Duration interval = DurationParser.parse(
                configRegistry.get(ConfigType.CONFIGURATION).getString("data.auto_save_interval", "10 minutes")
        );

        if (!interval.isZero() && !interval.isNegative()) {
            scheduler.runTimerAsync(
                    userManager::flush,
                    DurationParser.toTicks(interval),
                    DurationParser.toTicks(interval)
            );
        }

        this.chatProcessor = ExtensionManager.register();

        LeetMap.load(configRegistry);
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

        MistExecutors.shutdown();
    }

    public void reload() {
        this.configRegistry.reloadAll();
        this.chatProcessor = ExtensionManager.register();

        LeetMap.load(this.configRegistry);
        RegexGenerator.index();

        this.announcements.reload();
    }
}
