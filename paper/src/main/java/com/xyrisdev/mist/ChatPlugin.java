package com.xyrisdev.mist;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
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
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import com.xyrisdev.mist.util.time.IntervalParseUtil;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

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

        this.configRegistry = ConfigRegistry.load();
        this.folia = new FoliaLib(this);
        this.scheduler = folia.getScheduler();

        // database
        this.database = DatabaseProviders.create(this.configRegistry.get(ConfigType.CONFIGURATION));
        this.database.connect();

        // user cache & manager
        this.userManager = new ChatUserManager(this.database.users());
        this.userManager.autoFlush(
                IntervalParseUtil.parse(
                        this.configRegistry.get(ConfigType.CONFIGURATION)
                                .getString("data.auto_save_interval", "5m")
                )
        );

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
            this.userManager.shutdown();
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
