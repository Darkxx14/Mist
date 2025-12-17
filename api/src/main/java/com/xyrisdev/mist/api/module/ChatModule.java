package com.xyrisdev.mist.api.module;

import com.xyrisdev.mist.api.context.ChatContext;
import org.jetbrains.annotations.NotNull;

public interface ChatModule {

	int priority();

	void handle(@NotNull ChatContext context);
}
