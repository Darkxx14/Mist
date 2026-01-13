package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.util.matcher.SimilarityMatcher;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.paper.util.sender.Source;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public class SimilarityCommand {

	@Command("mist similarity <s1> <s2>")
	@Permission("mist.command.similarity")
	public void similarity(Source sender, String s1, String s2) {
		final double similarity = SimilarityMatcher.similarity(s1, s2);

		final double threshold = Mist.INSTANCE.config()
				.get(ConfigType.CHAT_FILTER)
				.getDouble("similarity.threshold", 0.60);

		MistMessage.create(sender.source())
				.id("mist_similarity")
				.placeholder("similarity", String.format("%.2f", similarity * 100))
				.placeholder("threshold", String.format("%.2f", threshold * 100))
				.placeholder("result", similarity >= threshold ? "Blocked" : "Allowed")
				.send();
	}
}
