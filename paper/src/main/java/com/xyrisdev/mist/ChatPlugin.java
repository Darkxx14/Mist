package com.xyrisdev.mist;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.xyrisdev.library.AbstractPlugin;
import com.xyrisdev.library.lib.Library;
import com.xyrisdev.library.lib.feature.FeatureFlags;
import com.xyrisdev.library.lib.feature.FeatureRegistry;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.command.MistCommandManager;
import com.xyrisdev.mist.extension.ExtensionManager;
import com.xyrisdev.mist.hook.HookManager;
import com.xyrisdev.mist.listener.AsyncChatListener;
import com.xyrisdev.mist.listener.PlayerQuitListener;
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
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
        this.chatProcessor = ExtensionManager.register();

        LeetMap.load(this.configRegistry);
        RegexGenerator.index();

        HookManager.of().load(this);

        AsyncChatListener.listener().register();
        PlayerQuitListener.listener().register();

        MistCommandManager.of(this);
    }

    @Override
    protected void shutdown() {
        HookManager.of().unload();

        if (this.folia != null) {
            this.folia.getScheduler().cancelAllTasks();
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
    }
}
