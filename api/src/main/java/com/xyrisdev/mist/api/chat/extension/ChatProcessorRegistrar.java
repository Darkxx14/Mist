package com.xyrisdev.mist.api.chat.extension;

import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessorStage;

public interface ChatProcessorRegistrar {

	void register(int order, ChatProcessorStage stage);
}
