package com.xyrisdev.mist.chat.processor;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessorStage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultChatProcessor implements ChatProcessor {

	private final List<ChatProcessorStage> stages;

	public DefaultChatProcessor(List<ChatProcessorStage> stages) {
		this.stages = List.copyOf(stages);
	}

	@Override
	public void process(@NotNull ChatContext context) {
		for (ChatProcessorStage stage : stages) {
			stage.process(context);

			if (context.result() == ChatResult.CANCEL) {
				return;
			}
		}
	}
}
