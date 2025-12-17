package com.xyrisdev.mist.api.processor;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.api.module.ChatModule;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ChatProcessor {

	private final List<ChatModule> modules;

	public ChatProcessor(@NotNull List<ChatModule> modules) {
		this.modules = modules;
	}

	public void process(@NotNull ChatContext context) {
		for (ChatModule module : modules) {
			if (context.cancelled()) {
				return;
			}

			module.handle(context);
		}
	}
}
