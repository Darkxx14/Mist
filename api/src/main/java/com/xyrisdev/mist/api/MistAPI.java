package com.xyrisdev.mist.api;

import com.xyrisdev.mist.api.chat.extension.registry.MistExtensionRegistry;
import com.xyrisdev.mist.api.event.MistEventBus;
import com.xyrisdev.mist.api.event.internal.impl.MistEventBusImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Main entry point for accessing Mist's public API.
 *
 * <p>This class acts as a central facade for all exposed Mist systems.</p>
 *
 * <p>All returned APIs are managed by Mist and remain stable across
 * versions unless stated otherwise.</p>
 *
 * @since 1.0.0
 */
public final class MistAPI {

	private static final MistExtensionRegistry EXTENSIONS = new MistExtensionRegistry();
	private static final MistEventBus EVENTBUS = new MistEventBusImpl();

	private MistAPI() {}

	/**
	 * Returns the registry responsible for managing chat extensions.
	 *
	 * <p>The extension registry allows registering and executing
	 * {@code @MistExtension}-annotated handlers during chat processing.</p>
	 *
	 * @return the global chat extension registry
	 * @since 1.0.0
	 */
	public static @NotNull MistExtensionRegistry extensions() {
		return EXTENSIONS;
	}

	public static @NotNull MistEventBus eventBus() {
		return EVENTBUS;
	}
}
