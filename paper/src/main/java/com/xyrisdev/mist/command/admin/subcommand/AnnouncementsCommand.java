package com.xyrisdev.mist.command.admin.subcommand;

import com.xyrisdev.mist.ChatPlugin;
import com.xyrisdev.mist.command.parser.AnnouncementParser;
import com.xyrisdev.mist.misc.announcement.AnnouncementService;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public class AnnouncementsCommand {

	private final AnnouncementService service = ChatPlugin.instance().announcements();

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(
				root.literal("announcements")
						.permission("mist.command.announcements")
						.literal("next")
						.handler(this::next)
		);

		manager.command(
				root.literal("announcements")
						.permission("mist.command.announcements")
						.literal("setnext")
						.required(
								"announcement",
								ParserDescriptor.of(new AnnouncementParser(), Announcement.class)
						)
						.handler(this::setNext)
		);

		manager.command(
				root.literal("announcements")
						.permission("mist.command.announcements")
						.literal("forcenow")
						.required(
								"announcement",
								ParserDescriptor.of(new AnnouncementParser(), Announcement.class)
						)
						.handler(this::forceNow)
		);
	}

	private void next(@NotNull CommandContext<Source> ctx) {
		service.nextPreview().ifPresentOrElse(
				a -> MistMessage.create(ctx.sender().source())
						.id("mist_announcement_next")
						.placeholder("name", a.name())
						.send(),
				() -> MistMessage.create(ctx.sender().source())
						.id("mist_announcement_none")
						.send()
		);
	}

	private void setNext(@NotNull CommandContext<Source> ctx) {
		final Announcement announcement = ctx.get("announcement");

		service.setNext(announcement.name());

		MistMessage.create(ctx.sender().source())
				.id("mist_announcement_setnext")
				.placeholder("name", announcement.name())
				.send();
	}

	private void forceNow(@NotNull CommandContext<Source> ctx) {
		if (!(ctx.sender().source() instanceof Player player)) {
			return;
		}

		final Announcement announcement = ctx.get("announcement");

		service.forceNow(announcement.name());

		MistMessage.create(player)
				.id("mist_announcement_forced")
				.placeholder("name", announcement.name())
				.send();
	}
}
