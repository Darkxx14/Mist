package com.xyrisdev.mist.command.admin.parser;

import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigPathParser<C> implements ArgumentParser<C, String>, BlockingSuggestionProvider.Strings<C> {

	private final ConfigRegistry registry;

	public ConfigPathParser(final @NotNull ConfigRegistry registry) {
		this.registry = registry;
	}

	@Override
	public @NotNull ArgumentParseResult<String> parse(@NotNull CommandContext<C> context, @NotNull CommandInput input) {
		final String value = input.peekString();

		if (!registry.all().contains(value)) {
			return ArgumentParseResult.failure(
					new IllegalArgumentException(
							"Unknown config file: " + value
					)
			);
		}

		input.readString();
		return ArgumentParseResult.success(value);
	}

	@Override
	public @NotNull List<String> stringSuggestions(@NotNull CommandContext<C> context, @NotNull CommandInput input) {
		final String current = input.peekString();

		return registry.all().stream()
				.filter(path -> path.startsWith(current))
				.collect(Collectors.toList());
	}
}
