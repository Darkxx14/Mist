package com.xyrisdev.mist.command.internal.argument;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.command.internal.exception.HandledParseException;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.annotations.parser.Parser;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public class AnnouncementArgument {

	@Parser(suggestions = "announcement")
	public Announcement parse(CommandContext<Source> ctx, @NotNull CommandInput input) {
		final String name = input.readString();

		return Mist.INSTANCE.announcements()
				.find(name)
				.orElseThrow(() -> HandledParseException.handle(
						name,
						() -> MistMessage.create(ctx.sender().source())
								.id("mist_announcement_invalid")
								.placeholder("name", name)
								.send()
				));
	}

	@Suggestions("announcement")
	public List<String> suggestions() {
		return Mist.INSTANCE.announcements().announcementNames();
	}
}
