package com.xyrisdev.mist;

import com.xyrisdev.library.AbstractPlugin;

public final class MistPlugin extends AbstractPlugin {

    @Override
    protected void load() {
        Mist.load(this);
    }

    @Override
    protected void run() {
	    Mist.INSTANCE.start();
    }

    @Override
    protected void shutdown() {
	    Mist.INSTANCE.shutdown();
    }
}
