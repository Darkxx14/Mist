package com.xyrisdev.mist.api.processor;

import com.xyrisdev.mist.api.context.ChatContext;

@FunctionalInterface
public interface ChatStage {
	void process(ChatContext context);
}
