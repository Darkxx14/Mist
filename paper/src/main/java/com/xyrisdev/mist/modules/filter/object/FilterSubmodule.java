package com.xyrisdev.mist.modules.filter.object;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface FilterSubmodule {

	@NotNull FilterResult handle(@NotNull UUID id, @NotNull String message);

	int priority();

	boolean enabled();
}
