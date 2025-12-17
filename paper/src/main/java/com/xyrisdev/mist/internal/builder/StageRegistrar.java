package com.xyrisdev.mist.internal.builder;

import com.xyrisdev.mist.api.processor.ChatStage;
import com.xyrisdev.mist.internal.module.ChatModule;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface StageRegistrar {

	void stage(@NotNull ChatStage stage, int priority);

	default StageRegistrar module(@NotNull ChatModule module) {
		module.register(this);
		return this;
	}
}