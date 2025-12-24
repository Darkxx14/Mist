package com.xyrisdev.mist.chat.bootstrap;

import com.xyrisdev.mist.api.chat.extension.ChatProcessorExtension;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.chat.extension.MistChatProcessorRegistrar;
import com.xyrisdev.mist.chat.processor.builder.ChatProcessorBuilder;

import java.util.ServiceLoader;

public class ChatProcessorBootstrap {

	public static ChatProcessor bootstrap() {
		final ChatProcessorBuilder builder = new ChatProcessorBuilder();
		final MistChatProcessorRegistrar registrar = new MistChatProcessorRegistrar(builder);

		ServiceLoader.load(ChatProcessorExtension.class)
				.forEach(extension -> extension.register(registrar));

		return builder.build();
	}
}
