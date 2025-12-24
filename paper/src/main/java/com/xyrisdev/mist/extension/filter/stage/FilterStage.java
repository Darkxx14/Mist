package com.xyrisdev.mist.extension.filter.stage;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.filter.config.FilterConfiguration;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import org.jetbrains.annotations.NotNull;

public class FilterStage implements ChatProcessStage {

	private final FilterConfiguration config;

	public FilterStage(@NotNull FilterConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext ctx) {
		for (FilterRule rule : config.rules()) {

			final FilterResult result = rule.process(ctx);

			if (result.cancel()) {
				ctx.cancel();
				return;
			}

			if (result.modifiedMessage() != null) {
				ctx.plain(result.modifiedMessage());
			}
		}
	}
}
