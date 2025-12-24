package com.xyrisdev.mist.command.admin.subcommand;

import com.xyrisdev.mist.util.message.MistMessage;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import com.xyrisdev.mist.util.regex.RegexHealthAnalyzer;
import net.kyori.adventure.text.event.ClickEvent;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;
import org.jetbrains.annotations.NotNull;

public class RegexCommand {

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(
				root.literal("regex")
						.permission("mist.commands.regex")
						.literal("generate")
						.required("text", StringParser.greedyStringParser())
						.handler(this::generate)
		);

		manager.command(
				root.literal("regex")
						.permission("mist.commands.regex")
						.literal("check")
						.required("regex", StringParser.greedyStringParser())
						.handler(this::check)
		);
	}

	private void generate(@NotNull CommandContext<Source> ctx) {
		final String input = ctx.get("text");

		final String regex = RegexGenerator.generate(input);

		MistMessage.create(ctx.sender().source())
				.id("mist_regex_generate")
				.placeholder("input", input)
				.placeholder("regex", regex)
				.interceptor(component ->
						component.clickEvent(ClickEvent.copyToClipboard(regex))
				)
				.send();
	}

	private void check(@NotNull CommandContext<Source> ctx) {
		final String regex = ctx.get("regex");
		final RegexHealthAnalyzer.Result r = RegexHealthAnalyzer.analyze(regex);

		MistMessage.create(ctx.sender().source())
				.id("mist_regex_check")
				.placeholder("score", String.valueOf(r.score()))
				.placeholder("verdict", r.verdict().name())
				.placeholder("risk", r.risk().name())
				.placeholder(
						"notes",
						r.notes().isEmpty()
								? "Looks solid <3"
								: String.join(", ", r.notes())
				)
				.send();
	}
}
