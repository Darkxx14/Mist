package com.xyrisdev.mist.util.message.builder.object;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MessageContext {

	private final Map<String, String> stringPlaceholders;
	private final Map<String, Component> componentPlaceholders;

	public MessageContext(@NotNull Map<String, String> stringPlaceholders) {
		this.stringPlaceholders = stringPlaceholders;
		this.componentPlaceholders = new HashMap<>();
	}

	public void component(@NotNull String key, @NotNull Component component) {
		componentPlaceholders.put(key, component);
	}

	public @NotNull Component apply(@NotNull Component component) {
		Component result = component;

		for (Map.Entry<String, Component> entry : componentPlaceholders.entrySet()) {
			result = result.replaceText(builder ->
					builder.matchLiteral("<" + entry.getKey() + ">")
							.replacement(entry.getValue())
			);
		}

		for (Map.Entry<String, String> entry : stringPlaceholders.entrySet()) {
			result = result.replaceText(builder ->
					builder.matchLiteral("<" + entry.getKey() + ">")
							.replacement(entry.getValue())
			);
		}

		return result;
	}
}
