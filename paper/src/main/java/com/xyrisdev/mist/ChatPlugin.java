package com.xyrisdev.mist;

import com.xyrisdev.library.AbstractPlugin;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public final class ChatPlugin extends AbstractPlugin {

    @Getter
    private static ChatPlugin instance;
    private MistService service;

    @Override
    protected void load() {
        instance = this;
    }

    @Override
    protected void run() {
        this.service = new MistService(this);
        this.service.start();
    }

    @Override
    protected void shutdown() {
        if (this.service != null) {
            this.service.shutdown();
            this.service = null;
        }

        instance = null;
    }

    public void reload() {
        if (this.service != null) {
            this.service.reload();
        }
    }

    public static MistService service() {
        return instance.service;
    }
}
