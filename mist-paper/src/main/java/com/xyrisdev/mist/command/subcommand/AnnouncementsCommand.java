package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.misc.announcement.object.Announcement;
import com.xyrisdev.mist.util.message.MistMessage;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.paper.util.sender.Source;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("unused")
public class AnnouncementsCommand {

	@Command("mist announcements next check")
	@Permission("mist.command.announcements")
	public void next(Source sender) {
		Mist.INSTANCE.announcements().previewNext().ifPresentOrElse(
				a -> MistMessage.create(sender.source())
						.id("mist_announcement_next")
						.placeholder("name", a.name())
						.send(),
				() -> MistMessage.create(sender.source())
						.id("mist_announcement_none")
						.send()
		);
	}

	@Command("mist announcements next set <announcement>")
	@Permission("mist.command.announcements")
	public void next(Source sender, Announcement announcement) {
		Mist.INSTANCE.announcements().setNext(announcement.name());

		MistMessage.create(sender.source())
				.id("mist_announcement_setnext")
				.placeholder("name", announcement.name())
				.send();
	}

	@Command("mist announcements force <announcement>")
	@Permission("mist.command.announcements")
	public void force(Source sender, Announcement announcement) {
		Mist.INSTANCE.announcements().force(announcement.name());

		MistMessage.create(sender.source())
				.id("mist_announcement_forced")
				.placeholder("name", announcement.name())
				.send();
	}
}
