package com.xyrisdev.mist.util.styling.color;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public final class MistColor {

	/* ---------- palette ---------- */

	public static final String GRADIENT = "#9aa3ad:#cfe0eb";

	public static final String SUCCESS = "#72ff59";
	public static final String ERROR   = "#ff5959";
	public static final String WARNING = "#fff159";

	/* ---------- wrappers ---------- */

	@Contract(pure = true)
	public static @NotNull String gradient(final @NotNull String content) {
		return "<gradient:" + GRADIENT + ">" + content + "</gradient>";
	}

	@Contract(pure = true)
	public static @NotNull String success(final @NotNull String content) {
		return "<color:" + SUCCESS + ">" + content + "</color>";
	}

	@Contract(pure = true)
	public static @NotNull String error(final @NotNull String content) {
		return "<color:" + ERROR + ">" + content + "</color>";
	}

	@Contract(pure = true)
	public static @NotNull String warning(final @NotNull String content) {
		return "<color:" + WARNING + ">" + content + "</color>";
	}

	@Contract(pure = true)
	public static @NotNull String info(final @NotNull String content) {
		return "<gray>" + content + "</gray>";
	}
}
