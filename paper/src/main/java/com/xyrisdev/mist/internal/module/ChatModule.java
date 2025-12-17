package com.xyrisdev.mist.internal.module;

import com.xyrisdev.mist.internal.builder.StageRegistrar;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ChatModule {

	void register(@NotNull StageRegistrar registrar);
}
