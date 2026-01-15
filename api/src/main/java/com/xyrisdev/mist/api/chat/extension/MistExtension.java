package com.xyrisdev.mist.api.chat.extension;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark classes as Mist chat extensions.
 *
 * <p>Classes annotated with {@code @MistExtension} are automatically discovered and registered
 * with the extension system. Each extension must have a unique ID and a descriptive name.
 * Extensions are executed in the chat processing pipeline based on their priority configuration.</p>
 *
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MistExtension {

	/**
	 * The unique identifier for this extension.
	 *
	 * <p>This ID is used in configuration files to set priorities and must be unique
	 * across all registered extensions.</p>
	 *
	 * @return the extension's unique ID
	 */
	@NotNull String id();

	/**
	 * A human-readable name for this extension.
	 *
	 * <p>This name is used for logging and debugging purposes.</p>
	 *
	 * @return the extension's display name
	 */
	@NotNull String name();
}