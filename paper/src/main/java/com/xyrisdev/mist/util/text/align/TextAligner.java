package com.xyrisdev.mist.util.text.align;

import com.xyrisdev.mist.util.text.align.internal.GlyphWidthVisitor;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public final class TextAligner {

	private static final int CHAT_CENTER_PX = 154;
	private static final int CONSOLE_WIDTH_CHARS = 80;

	public static final MinecraftFont FONT = MinecraftFont.Font;
	public static final int UNKNOWN_WIDTH = FONT.getChar('?').getWidth();

	private static final PlainTextComponentSerializer PLAIN =
			PlainTextComponentSerializer.plainText();

	public static @NotNull Component center(
			@NotNull Audience audience,
			@NotNull Component component
	) {
		if (isConsole(audience)) {
			return centerConsole(component);
		}

		return centerChat(component);
	}

	public static @NotNull Component centerChat(@NotNull Component component) {
		final int messagePxSize = measure(component);
		final int halved = messagePxSize / 2;
		final int toCompensate = CHAT_CENTER_PX - halved;

		if (toCompensate <= 0) {
			return component;
		}

		final int spaceAdvance = FONT.getChar(' ').getWidth() + 1;
		int compensated = 0;

		final StringBuilder padding = new StringBuilder();

		while (compensated < toCompensate) {
			padding.append(' ');
			compensated += spaceAdvance;
		}

		return Component.text(padding.toString()).append(component);
	}

	public static @NotNull Component centerConsole(@NotNull Component component) {
		final String plain = PLAIN.serialize(component);
		final int length = plain.length();

		if (length >= CONSOLE_WIDTH_CHARS) {
			return component;
		}

		final int padding = (CONSOLE_WIDTH_CHARS - length) / 2;
		return Component.text(" ".repeat(padding))
				.append(Component.text(plain));
	}

	private static int measure(@NotNull Component component) {
		final GlyphWidthVisitor visitor = new GlyphWidthVisitor();

		ComponentFlattener.basic().flatten(component, visitor);
		return visitor.width();
	}

	private static boolean isConsole(@NotNull Audience audience) {
		return audience == Bukkit.getConsoleSender()
				|| audience instanceof ConsoleCommandSender;
	}
}
