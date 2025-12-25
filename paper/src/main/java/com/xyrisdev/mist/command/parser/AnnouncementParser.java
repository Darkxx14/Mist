package com.xyrisdev.mist.command.parser;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.ArgumentParseException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unchecked")
public class AnnouncementParser implements ArgumentParser<Source, Announcement> {

	@Override
	public @NotNull ArgumentParseResult<Announcement> parse(@NotNull CommandContext<Source> ctx, @NotNull CommandInput input) {
		final String name = input.readString();

		return ChatPlugin.instance()
				.announcements()
				.find(name)
				.map(ArgumentParseResult::success)
				.orElseGet(() -> ArgumentParseResult.failure(
						new ArgumentParseException(
								new IllegalArgumentException("Unknown announcement: " + name),
								name,
								(List<CommandComponent<?>>) (List<?>) ctx.command().components()
						)
				));
	}

	@Override
	public @NotNull BlockingSuggestionProvider.Strings<Source> suggestionProvider() {
		return (ctx, input) ->
				ChatPlugin.instance()
						.announcements()
						.announcementNames();
	}
}
