package com.xyrisdev.mist.util.text.tags;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SmallCapsTag implements Modifying {

	private final Map<Character, Character> map;
	private final TextReplacementConfig replacement;

	public SmallCapsTag() {
		this.map = map();
		this.replacement = TextReplacementConfig.builder()
				.match("[A-Za-zÀ-ÿ]")
				.replacement((match, builder) ->
						Component.text(transform(match.group()))
				)
				.build();
	}

	@Override
	public @NotNull Component apply(@NotNull Component current, int depth) {
		if (depth != 0) {
			return Component.empty();
		}

		return current.replaceText(replacement);
	}

	private @NotNull String transform(final @NotNull String input) {
		final StringBuilder out = new StringBuilder(input.length());

		for (char c : input.toCharArray()) {
			out.append(map.getOrDefault(c, c));
		}

		return out.toString();
	}

	private static @NotNull Map<Character, Character> map() {
		final Map<Character, Character> map = new HashMap<>();

		final String normal = "abcdefghijklmnopqrstuvwxyz";
		final String small  = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘꞯʀsᴛᴜᴠᴡxʏᴢ";

		for (int i = 0; i < normal.length(); i++) {
			map.put(normal.charAt(i), small.charAt(i));
			map.put(Character.toUpperCase(normal.charAt(i)), small.charAt(i));
		}

		return map;
	}
}
