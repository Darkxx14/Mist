package com.xyrisdev.mist.api.processor;

import com.xyrisdev.mist.api.context.ChatContext;

import java.util.List;

public final class ChatProcessor {

	private final List<ChatStage> stages;

	public ChatProcessor(List<ChatStage> stages) {
		this.stages = stages;
	}

	public void process(ChatContext context) {
		for (ChatStage stage : stages) {
			stage.process(context);

			if (context.cancelled()) {
				return;
			}
		}
	}
}
