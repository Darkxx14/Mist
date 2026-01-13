package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.util.message.MistMessage;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import com.xyrisdev.mist.util.regex.RegexHealthAnalyzer;
import net.kyori.adventure.text.event.ClickEvent;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.paper.util.sender.Source;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public class RegexCommand {

	@Command("mist regex generate <text>")
	@Permission("mist.command.regex")
	public void generate(Source sender, String text) {
		final String regex = RegexGenerator.generate(text);

		MistMessage.create(sender.source())
				.id("mist_regex_generate")
				.placeholder("input", text)
				.placeholder("regex", regex)
				.interceptor(component ->
						component.clickEvent(ClickEvent.copyToClipboard(regex))
				)
				.send();
	}

	@Command("mist regex analyze <regex>")
	@Permission("mist.command.regex")
	public void analyze(Source sender, String regex) {
		final RegexHealthAnalyzer.Result r = RegexHealthAnalyzer.analyze(regex);

		MistMessage.create(sender.source())
				.id("mist_regex_analyze")
				.placeholder("score", String.valueOf(r.score()))
				.placeholder("verdict", r.verdict().name())
				.placeholder("risk", r.risk().name())
				.placeholder(
						"notes", r.notes().isEmpty()
								? "Looks solid <3"
								: String.join(", ", r.notes())
				)
				.send();
	}
}
