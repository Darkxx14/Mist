package com.xyrisdev.mist.extension.filter.stage;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessorStage;
import com.xyrisdev.mist.extension.filter.config.FilterConfiguration;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import org.jetbrains.annotations.NotNull;

public class FilterStage implements ChatProcessorStage {

	private final FilterConfiguration config;

	public FilterStage(@NotNull FilterConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext context) {
		for (FilterRule rule : config.rules()) {

			final FilterResult result = rule.process(context);

			if (result.cancel()) {
				context.cancel();
				return;
			}

			if (result.modifiedMessage() != null) {
				context.plain(result.modifiedMessage());
			}
		}
	}
}
