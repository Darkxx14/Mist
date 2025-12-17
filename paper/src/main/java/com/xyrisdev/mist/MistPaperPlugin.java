package com.xyrisdev.mist;

import com.xyrisdev.library.AbstractPlugin;
import com.xyrisdev.library.lib.Library;
import com.xyrisdev.library.lib.feature.FeatureFlags;
import com.xyrisdev.library.lib.feature.FeatureRegistry;
import com.xyrisdev.library.scheduler.XScheduler;
import com.xyrisdev.library.scheduler.scheduling.schedulers.TaskScheduler;
import com.xyrisdev.mist.api.processor.ChatProcessor;
import com.xyrisdev.mist.listener.AsyncChatListener;
import com.xyrisdev.mist.modules.ChatBootstrap;
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
    private ChatProcessor processor;

    @Override
    protected void load() {
        instance = this;
    }

    @Override
    protected void run() {
        Library.of(this, "mist-paper");

        this.configRegistry = ConfigRegistry.load();
        this.scheduler = XScheduler.of(this);
        this.processor = ChatBootstrap.bootstrap(this.configRegistry);

        ChatBootstrap.bootstrap(this.configRegistry);

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
}
