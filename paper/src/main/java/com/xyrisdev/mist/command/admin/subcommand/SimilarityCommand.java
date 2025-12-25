package com.xyrisdev.mist.command.admin.subcommand;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.util.matcher.SimilarityMatcher;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;
import org.jetbrains.annotations.NotNull;

public class SimilarityCommand {

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(
				root.literal("similarity")
						.permission("mist.command.similarity")
						.required("s1", StringParser.stringParser())
						.required("s2", StringParser.stringParser())
						.handler(this::similarity)
		);
	}

	private void similarity(@NotNull CommandContext<Source> ctx) {
		final String s1 = ctx.get("s1");
		final String s2 = ctx.get("s2");

		final double similarity = SimilarityMatcher.similarity(s1, s2);

		final double threshold = ChatPlugin.instance()
				.configRegistry()
				.get(ConfigType.CHAT_FILTER)
				.getDouble("similarity.threshold", 0.60);

		final boolean blocked = similarity >= threshold;

		MistMessage.create(ctx.sender().source())
				.id("mist_similarity")
				.placeholder("similarity", String.format("%.2f", similarity * 100))
				.placeholder("threshold", String.format("%.2f", threshold * 100))
				.placeholder("result", blocked ? "Blocked" : "Allowed")
				.send();
	}
}
