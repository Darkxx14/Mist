package com.xyrisdev.mist.command.internal.parser;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.command.internal.exception.HandledParseException;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.jetbrains.annotations.NotNull;

public class AnnouncementParser implements ArgumentParser<Source, Announcement> {

	@Override
	public @NotNull ArgumentParseResult<Announcement> parse(@NotNull CommandContext<Source> ctx, @NotNull CommandInput input) {
		final String name = input.readString();

		return ChatPlugin.instance()
				.announcements()
				.find(name)
				.map(ArgumentParseResult::success)
				.orElseThrow(() -> HandledParseException.handle(
						name,
						() -> MistMessage.create(ctx.sender().source())
								.id("mist_announcement_invalid")
								.placeholder("name", name)
								.send()
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
