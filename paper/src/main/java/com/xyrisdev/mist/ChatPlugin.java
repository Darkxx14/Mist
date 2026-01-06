package com.xyrisdev.mist;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.util.FoliaLibOptions;
import com.xyrisdev.library.AbstractPlugin;
import com.xyrisdev.library.lib.Library;
import com.xyrisdev.library.lib.feature.FeatureFlags;
import com.xyrisdev.library.lib.feature.FeatureRegistry;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.command.internal.MistCommandManager;
import com.xyrisdev.mist.data.DatabaseProvider;
import com.xyrisdev.mist.data.DatabaseProviders;
import com.xyrisdev.mist.extension.ExtensionManager;
import com.xyrisdev.mist.hook.impl.*;
import com.xyrisdev.mist.listener.AsyncChatListener;
import com.xyrisdev.mist.listener.PlayerJoinListener;
import com.xyrisdev.mist.listener.PlayerQuitListener;
import com.xyrisdev.mist.misc.announcement.AnnouncementService;
import com.xyrisdev.mist.user.ChatUserManager;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.config.registry.ConfigRegistry;
import com.xyrisdev.mist.util.logger.MistLogger;
import com.xyrisdev.mist.util.logger.suppress.FoliaLibSuppressor;
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import com.xyrisdev.mist.util.thread.MistExecutors;
import com.xyrisdev.mist.util.time.DurationParser;
import io.papermc.paper.plugin.configuration.PluginMeta;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@Accessors(fluent = true)
public final class ChatPlugin extends AbstractPlugin {

    @Getter
    private static ChatPlugin instance;

    @Getter
    private ConfigRegistry configRegistry;

    @Getter
    private FoliaLib folia;

    @Getter
    private PlatformScheduler scheduler;

    @Getter
    private ChatProcessor chatProcessor;

    @Getter
    private AnnouncementService announcements;

    @Getter
    private DatabaseProvider database;

    @Getter
    private ChatUserManager userManager;

    @Override
    protected void load() {
        instance = this;
    }

    @Override
    protected void run() {
        Library.of(this, "mist-paper");

        final PluginMeta meta = this.getPluginMeta();
        final Server server = this.server();

        MistLogger.infoNoPrefix("<white>Mist</white> <gray>v" + meta.getVersion() + "</gray>");
        MistLogger.infoNoPrefix("<gray>" + server.getName() + "</gray> <dark_gray>·</dark_gray> <gray>" + server.getBukkitVersion() + "</gray>");

        this.configRegistry = ConfigRegistry.load();
        this.folia = FoliaLibSuppressor.suppress(this, () -> new FoliaLib(this));
        this.scheduler = this.folia.getScheduler();

        MistExecutors.start();

        // database
        this.database = DatabaseProviders.create(this.configRegistry.get(ConfigType.CONFIGURATION));
        this.database.connect();

        // user cache & manager
        this.userManager = new ChatUserManager(this.database.users());

        final Duration interval = DurationParser.parse(
                this.configRegistry.get(ConfigType.CONFIGURATION).getString("data.auto_save_interval", "10 minutes")
        );

        if (!interval.isZero() && !interval.isNegative()) {
            this.scheduler.runTimerAsync(
                    userManager::flush,
                    DurationParser.toTicks(interval),
                    DurationParser.toTicks(interval)
            );
        }

        // chat processor
        this.chatProcessor = ExtensionManager.register();

        // matcher utilities
        LeetMap.load(this.configRegistry);
        RegexGenerator.index();

        // plugin hooks
        LuckPermsHook.hook().register();
        PlaceholderAPIHook.hook().register();

        // misc features
        this.announcements = new AnnouncementService(this);
        this.announcements.start();

        // listeners
        AsyncChatListener.listener().register();
        PlayerJoinListener.listener().register();
        PlayerQuitListener.listener().register();

        // commands
        MistCommandManager.of(this);
    }

    @Override
    protected void shutdown() {
        if (this.announcements != null) {
            this.announcements.stop();
            this.announcements = null;
        }

        if (this.userManager != null) {
            this.userManager.flush();
            this.userManager.invalidateAll();
            this.userManager = null;
        }

        if (this.database != null) {
            this.database.shutdown();
            this.database = null;
        }

        if (this.scheduler != null) {
            this.scheduler.cancelAllTasks();
            this.scheduler = null;
        }

        MistExecutors.shutdown();
        instance = null;
    }

    @Override
    protected void feature(@NotNull FeatureRegistry registry) {
        registry.registrar()
                .disable(FeatureFlags.Conversation.PROCESS)
                .disable(FeatureFlags.CallBack.PROCESS)
                .disable(FeatureFlags.WorldGuard.REGION)
                .disable(FeatureFlags.Game.CRYSTAL)
                .disable(FeatureFlags.Game.ANCHOR);
    }

    public void reload() {
        this.configRegistry.reloadAll();
        this.chatProcessor = ExtensionManager.register();

        LeetMap.load(this.configRegistry);
        RegexGenerator.index();

        this.announcements.reload();
    }
}
