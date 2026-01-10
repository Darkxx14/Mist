package com.xyrisdev.mist.api.chat.processor;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.registry.MistExtensionRegistry;
import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ChatProcessor {

	private final List<String> extensions;

	public ChatProcessor(@NotNull List<String> extensions) {
		this.extensions = extensions;
	}

	public void process(@NotNull ChatContext ctx) {
		for (String extensionId : this.extensions) {
			MistExtensionRegistry.process(ctx, extensionId);

			if (ctx.result() == ChatResult.CANCEL) {
				return;
			}
		}
	}
}