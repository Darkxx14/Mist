package com.xyrisdev.mist.internal.builder;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface GroupConfigurer {

	void configure(@NotNull StageRegistrar registrar);
}
