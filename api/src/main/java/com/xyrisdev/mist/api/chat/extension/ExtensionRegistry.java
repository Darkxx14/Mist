package com.xyrisdev.mist.api.chat.extension;

import com.xyrisdev.mist.api.chat.extension.meta.ExtensionMetadata;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.api.chat.processor.stage.OrderedStage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExtensionRegistry {

	private final List<OrderedStage> stages = new ArrayList<>();

	public static @NotNull ChatProcessor create(@NotNull Consumer<ExtensionRegistry> registrar) {
		final ExtensionRegistry registry = new ExtensionRegistry();
		registrar.accept(registry);

		return new ChatProcessor(registry.stages);
	}

	public void register(int order, @NotNull ChatProcessStage stage) {
		stages.add(new OrderedStage(order, stage));
	}

	public void register(int order, @NotNull ExtensionMetadata extension) {
		try {
			final Object config = extension.configuration();
			ChatProcessStage stage;

			if (config != null) {
				stage = extension.stage()
						.getDeclaredConstructor(config.getClass())
						.newInstance(config);
			} else {
				stage = extension.stage()
						.getDeclaredConstructor()
						.newInstance();
			}

			register(order, stage);
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate extension: " + extension.name(), e);
		}
	}
}