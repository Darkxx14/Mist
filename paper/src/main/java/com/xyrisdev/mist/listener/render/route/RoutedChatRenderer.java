package com.xyrisdev.mist.listener.render.route;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.listener.render.ChatFormatRenderer;
import io.papermc.paper.chat.ChatRenderer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RoutedChatRenderer {

	@SuppressWarnings("OverrideOnly")
	public static @NotNull ChatRenderer route(@NotNull ChatContext context) {
		final ChatRenderer base = ChatRenderer.viewerUnaware(ChatFormatRenderer.render(context));

		return (source, displayName, message, viewer) -> {

			if (viewer instanceof Player target) {
				// future routing:
				// if (context.isStaffChat() && !target.hasPermission("mist.staff")) {
				//     return Component.empty();
				// }
			}

			return base.render(source, displayName, message, viewer);
		};
	}
}
