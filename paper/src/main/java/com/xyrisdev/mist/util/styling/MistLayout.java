package com.xyrisdev.mist.util.styling;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.util.styling.layout.LineStyle;
import com.xyrisdev.mist.util.styling.layout.LineType;
import com.xyrisdev.mist.util.text.MistTextParser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MistLayout {

	private final Audience audience;
	private final List<Component> lines = new ArrayList<>();

	private MistLayout(final Audience audience) {
		this.audience = audience;
	}

	public static @NotNull MistLayout create(@NotNull Audience audience) {
		return new MistLayout(audience);
	}

	public static @NotNull Segment part(
			@NotNull LineStyle style,
			@NotNull String content
	) {
		return new Segment(style, content);
	}

	public static @NotNull Segment part(
			@NotNull String color,
			@NotNull String content
	) {
		return new Segment(color, content);
	}

	public MistLayout header() {
		return line(
				LineType.CENTER,
				part(LineStyle.GRADIENT, "<bold>Mist</bold>")
		);
	}

	public MistLayout version() {
		line(
				LineType.CENTER,
				part(LineStyle.MUTED, "v" + MistPaperPlugin.instance().getPluginMeta().getVersion())
		);
		return blank();
	}

	@Contract("_, _ -> this")
	public MistLayout line(
			@NotNull LineType type,
			@NotNull Segment @NotNull ... segments
	) {
		final StringBuilder builder = new StringBuilder();

		for (Segment segment : segments) {
			builder.append(segment.render());
		}

		String result = builder.toString();

		if (type == LineType.CENTER) {
			result = "<center>" + result + "</center>";
		}

		lines.add(MistTextParser.parse(audience, result));
		return this;
	}

	public MistLayout blank() {
		lines.add(Component.empty());
		return this;
	}

	public void send() {
		audience.sendMessage(
				Component.join(
						JoinConfiguration.separator(Component.newline()),
						lines
				)
		);
	}

	public static final class Segment {

		private final LineStyle style;
		private final String color;
		private final String content;

		private Segment(@NotNull LineStyle style, @NotNull String content) {
			this.style = style;
			this.color = null;
			this.content = content;
		}

		private Segment(@NotNull String color, @NotNull String content) {
			this.style = null;
			this.color = color;
			this.content = content;
		}

		private String render() {
			if (style != null) {
				return style.apply(content);
			}

			if (color.startsWith("#")) {
				return "<color:" + color + ">" + content + "</color>";
			}

			if (color.startsWith("<") && color.endsWith(">")) {
				final String tag = color.substring(1, color.length() - 1);
				return "<" + tag + ">" + content + "</" + tag + ">";
			}

			return content;
		}
	}
}
