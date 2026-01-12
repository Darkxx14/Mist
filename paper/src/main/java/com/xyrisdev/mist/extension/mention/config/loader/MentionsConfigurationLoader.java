package com.xyrisdev.mist.extension.mention.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.extension.mention.config.MentionsConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MentionsConfigurationLoader {

	private static final String def_pattern = "@([a-zA-Z0-9_]{1,16})";

	@Contract("_ -> new")
	public static @NotNull MentionsConfiguration load(@NotNull CachableConfiguration config) {
		final boolean enabled = config.getBoolean("enabled", true);
		final String pattern = config.getString("mentions.pattern", def_pattern);

		Pattern resolved;

		try {
			resolved = Pattern.compile(pattern);
		} catch (PatternSyntaxException ex) {
			resolved = Pattern.compile(def_pattern);
		}

		final String format = config.getString("mentions.format", "<color:#55FFFF>@<name></color>");

		return new MentionsConfiguration(
				enabled,
				resolved,
				format
		);
	}
}
