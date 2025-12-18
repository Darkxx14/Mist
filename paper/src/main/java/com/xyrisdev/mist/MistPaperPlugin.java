package com.xyrisdev.mist;

import com.xyrisdev.library.AbstractPlugin;
import com.xyrisdev.library.lib.Library;
import com.xyrisdev.library.lib.feature.FeatureFlags;
import com.xyrisdev.library.lib.feature.FeatureRegistry;
import com.xyrisdev.library.scheduler.XScheduler;
import com.xyrisdev.library.scheduler.scheduling.schedulers.TaskScheduler;
import com.xyrisdev.mist.api.processor.ChatProcessor;
import com.xyrisdev.mist.hook.LuckPermsHook;
import com.xyrisdev.mist.listener.AsyncChatListener;
import com.xyrisdev.mist.module.ModuleRegistrar;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public final class MistPaperPlugin extends AbstractPlugin {

    @Getter
    private static MistPaperPlugin instance;

    @Getter
    private ConfigRegistry configRegistry;

    @Getter
    private TaskScheduler scheduler;

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
        this.scheduler = XScheduler.of(this);
        this.chatProcessor = ModuleRegistrar.build();

        if (plugins().isPluginEnabled("LuckPerms")) {
            LuckPermsHook.init();
        } else {
            this.disable();
        }

        AsyncChatListener.listener().register();
    }

    @Override
    protected void shutdown() {
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
        this.chatProcessor = ModuleRegistrar.build();
    }
}
