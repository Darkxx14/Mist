package com.xyrisdev.mist.util.message.builder.object;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record MessageContext(@NotNull Map<String, String> placeholders) {

	public Component apply(@NotNull Component component) {
		Component result = component;

		for (Map.Entry<String, String> entry : placeholders.entrySet()) {
			result = result.replaceText(builder ->
					builder.matchLiteral("<" + entry.getKey() + ">")
							.replacement(entry.getValue())
			);
		}

		return result;
	}
}