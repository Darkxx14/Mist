package com.xyrisdev.mist.api.chat.extension.registry;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MistExtensionRegistry {

	private static final Map<String, Object> extensions = new ConcurrentHashMap<>();
	private static final Map<String, Method> handlers = new ConcurrentHashMap<>();

	public static void register(@NotNull Object extension) {
		final Class<?> clazz = extension.getClass();
		final MistExtension annotation = clazz.getAnnotation(MistExtension.class);

		if (annotation == null) {
			throw new IllegalArgumentException("extension must be annotated with @MistExtension");
		}

		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(ExtensionHandler.class) && method.getParameterCount() == 1
					&& method.getParameterTypes()[0] == ChatContext.class) {

				extensions.put(annotation.id(), extension);
				handlers.put(annotation.id(), method);
				method.setAccessible(true);
				break;
			}
		}
	}

	@SuppressWarnings("CallToPrintStackTrace")
	public static void process(ChatContext ctx, String extensionId) {
		final Object extension = extensions.get(extensionId);
		final Method handler = handlers.get(extensionId);

		if (extension != null && handler != null) {
			try {
				handler.invoke(extension, ctx);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}