package com.xyrisdev.mist.api.chat.processor;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.api.chat.processor.stage.OrderedStage;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class ChatProcessor {

	private final List<ChatProcessStage> stages;

	public ChatProcessor(@NotNull List<OrderedStage> orderedStages) {
		this.stages = orderedStages.stream()
				.sorted(Comparator.comparingInt(OrderedStage::order))
				.map(OrderedStage::stage)
				.toList();
	}

	public void process(@NotNull ChatContext ctx) {
		for (ChatProcessStage stage : stages) {
			stage.process(ctx);

			if (ctx.result() == ChatResult.CANCEL) {
				return;
			}
		}
	}
}