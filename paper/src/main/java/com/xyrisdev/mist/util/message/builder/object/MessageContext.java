package com.xyrisdev.mist.util.message.builder.object;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class MessageContext {

	private static final Pattern pattern = Pattern.compile("<(\\w+)>");

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
		if (componentPlaceholders.isEmpty() && stringPlaceholders.isEmpty()) {
			return interceptor != null ? interceptor.apply(component) : component;
		}

		final Component comp = component.replaceText(builder ->
				builder.match(pattern)
						.replacement((match, ctx) -> {
							final String key = match.group(1);

							final Component componentReplacement = componentPlaceholders.get(key);

							if (componentReplacement != null) {
								return componentReplacement;
							}

							final String stringReplacement = stringPlaceholders.get(key);

							if (stringReplacement != null) {
								return Component.text(stringReplacement);
							}

							return Component.text(match.group());
						})
		);

		return interceptor != null ? interceptor.apply(comp) : comp;
	}
}