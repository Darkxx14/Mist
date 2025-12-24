package com.xyrisdev.mist.api.chat.processor;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNull;

public interface ChatProcessor {

	void process(@NotNull ChatContext context);
}
