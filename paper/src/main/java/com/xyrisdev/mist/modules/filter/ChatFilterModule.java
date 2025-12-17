package com.xyrisdev.mist.modules.filter;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.api.module.ChatModule;
import com.xyrisdev.mist.modules.filter.config.FilterConfig;
import com.xyrisdev.mist.modules.filter.object.FilterResult;
import com.xyrisdev.mist.modules.filter.submodules.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ChatFilterModule implements ChatModule {

	private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
	private final ChatFilterPipeline pipeline;

	public ChatFilterModule(@NotNull FilterConfig config) {
		this.pipeline = new ChatFilterPipeline(List.of(
				new SimilaritySubmodule(config.similarity()),
				new AntiFloodSubmodule(config.antiFlood()),
				new AntiCapsSubmodule(config.antiCaps())
		));
	}

	@Override
	public int priority() {
		return 10;
	}

	@Override
	public void handle(@NotNull ChatContext context) {
		final Component original = context.message();
		final String plain = PLAIN.serialize(original);

		final FilterResult result = pipeline.process(
				context.player().getUniqueId(),
				plain
		);

		switch (result.decision()) {
			case BLOCK -> context.cancel();

			case MODIFY -> context.message(
					Component.text(result.modifiedMessage())
			);

			default -> {
				// NOOP
			}
		}
	}
}
