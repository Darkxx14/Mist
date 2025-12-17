package com.xyrisdev.mist.util.text.align.internal;

import com.xyrisdev.mist.util.text.align.TextAligner;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;

public final class GlyphWidthVisitor implements FlattenerListener {

	private final Deque<Style> styles = new ArrayDeque<>();
	private int width;

	@Override
	public void pushStyle(@NotNull Style style) {
		styles.push(style);
	}

	@Override
	public void component(@NotNull String text) {
		final boolean bold = isBold();

		for (char c : text.toCharArray()) {
			final var glyph = TextAligner.FONT.getChar(c);

			int charWidth = glyph != null
					? glyph.getWidth()
					: TextAligner.UNKNOWN_WIDTH;

			if (bold && c != ' ') {
				charWidth++;
			}

			width += charWidth + 1;
		}
	}

	@Override
	public void popStyle(@NotNull Style style) {
		styles.pop();
	}

	private boolean isBold() {
		for (Style style : styles) {
			if (style.decoration(TextDecoration.BOLD) == TextDecoration.State.TRUE) {
				return true;
			}
		}
		return false;
	}

	public int width() {
		return width;
	}
}
