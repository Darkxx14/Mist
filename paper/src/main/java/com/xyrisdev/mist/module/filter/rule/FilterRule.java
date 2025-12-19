package com.xyrisdev.mist.module.filter.rule;

import com.xyrisdev.mist.api.context.ChatContext;
import org.jetbrains.annotations.NotNull;

public interface FilterRule {

	boolean enabled();

	@NotNull
	FilterResult process(@NotNull ChatContext context);
}
