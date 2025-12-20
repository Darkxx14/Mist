package com.xyrisdev.mist.module.filter.stage;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.api.processor.ChatStage;
import com.xyrisdev.mist.module.filter.config.FilterConfiguration;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import org.jetbrains.annotations.NotNull;

public class FilterStage implements ChatStage {

	private final FilterConfiguration config;

	public FilterStage(@NotNull FilterConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext context) {
		for (FilterRule rule : config.rules()) {

			if (!rule.enabled()) {
				continue;
			}

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