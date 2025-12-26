package com.xyrisdev.mist.util.sql.object;

import org.jetbrains.annotations.NotNull;

public record SQLColumn(
		@NotNull String name,
		@NotNull String sql
) {}
