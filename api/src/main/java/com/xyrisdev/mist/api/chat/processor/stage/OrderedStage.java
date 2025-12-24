package com.xyrisdev.mist.api.chat.processor.stage;

import org.jetbrains.annotations.NotNull;

public record OrderedStage(
		int order,
		@NotNull ChatProcessStage stage)
{}