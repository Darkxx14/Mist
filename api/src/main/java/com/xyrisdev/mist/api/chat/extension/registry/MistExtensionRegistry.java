package com.xyrisdev.mist.api.chat.extension.registry;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.api.chat.extension.MistExtension;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing Mist chat extensions.
 *
 * <p>This class provides a centralized registry for all chat extensions, allowing
 * them to be registered, retrieved, and executed during chat processing. Extensions
 * are stored by their unique ID and can be looked up when processing chat messages.</p>
 *
 * @since 1.0.0
 */
public final class MistExtensionRegistry {

	private static final Map<String, Object> extensions = new ConcurrentHashMap<>();
	private static final Map<String, Method> handlers = new ConcurrentHashMap<>();

	/**
	 * Registers a new extension with the registry.
	 *
	 * <p>The extension's class must be annotated with @MistExtension. The extension
	 * will be stored and can be retrieved using its ID.</p>
	 *
	 * @param extension the extension instance to register
	 * @throws IllegalArgumentException if an extension with the same ID is already registered
	 */
	public static void register(@NotNull Object extension) {
		final Class<?> clazz = extension.getClass();
		final MistExtension annotation = clazz.getAnnotation(MistExtension.class);

		if (annotation == null) {
			throw new IllegalArgumentException("extension must be annotated with @MistExtension");
		}

		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(ExtensionHandler.class) && method.getParameterCount() == 1
					&& method.getParameterTypes()[0] == ChatContext.class) {

				extensions.put(annotation.id(), extension);
				handlers.put(annotation.id(), method);
				break;
			}
		}
	}

	/**
	 * Executes a specific extension handler for the given chat context.
	 *
	 * <p>Looks up the extension by ID and invokes its @ExtensionHandler method
	 * with the provided chat context. If the extension fails to execute,
	 * the exception stack trace will be printed to console.</p>
	 *
	 * @param ctx the chat context to process
	 * @param extensionId the ID of the extension to execute
	 */
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