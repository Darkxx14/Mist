package com.xyrisdev.mist.api.chat.processor.stage;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ChatProcessorStage {

	void process(@NotNull ChatContext context);
}
