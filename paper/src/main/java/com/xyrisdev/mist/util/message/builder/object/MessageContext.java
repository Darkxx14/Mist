package com.xyrisdev.mist.util.message.builder.object;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class MessageContext {

	private final Map<String, String> stringPlaceholders;
	private final Map<String, Component> componentPlaceholders;

	private UnaryOperator<Component> interceptor;

	public MessageContext(@NotNull Map<String, String> stringPlaceholders) {
		this.stringPlaceholders = stringPlaceholders;
		this.componentPlaceholders = new HashMap<>();
	}

	public void component(@NotNull String key, @NotNull Component component) {
		this.componentPlaceholders.put(key, component);
	}

	public void interceptor(@NotNull UnaryOperator<Component> interceptor) {
		this.interceptor = interceptor;
	}

	public @NotNull Component apply(@NotNull Component component) {
		Component result = component;

		for (Map.Entry<String, Component> entry : this.componentPlaceholders.entrySet()) {
			result = result.replaceText(builder ->
					builder.matchLiteral("<" + entry.getKey() + ">")
							.replacement(entry.getValue())
			);
		}

		for (Map.Entry<String, String> entry : this.stringPlaceholders.entrySet()) {
			result = result.replaceText(builder ->
					builder.matchLiteral("<" + entry.getKey() + ">")
							.replacement(entry.getValue())
			);
		}

		return this.interceptor != null ? this.interceptor.apply(result) : result;
	}
}
