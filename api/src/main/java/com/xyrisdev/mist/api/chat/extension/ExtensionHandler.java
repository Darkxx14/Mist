package com.xyrisdev.mist.api.chat.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark methods that handle chat processing in extensions.
 *
 * <p>Methods annotated with {@code @ExtensionHandler} are automatically discovered and invoked
 * during chat processing. The method must accept a ChatContext parameter and will be
 * called for each chat message that passes through the extension pipeline.</p>
 *
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtensionHandler {}