package com.xyrisdev.mist.api.chat.processor;

import com.xyrisdev.mist.api.MistAPI;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.result.ChatResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Processes chat messages through a pipeline of registered extensions.
 *
 * <p>The ChatProcessor executes chat extensions in a specific order defined by
 * the extension priorities configuration. Each extension can modify the message,
 * add attributes to the context, or cancel the processing entirely.</p>
 *
 * @since 1.0.0
 */
public record ChatProcessor(@NotNull List<String> extensions) {

	/**
	 * Processes a chat context through all registered extensions.
	 *
	 * <p>The method iterates through extensions in order, allowing each to
	 * process the context. If any extension cancels the processing, the loop
	 * terminates early.</p>
	 *
	 * @param ctx the chat context to process
	 */
	public void process(@NotNull ChatContext ctx) {
		for (String extensionId : this.extensions) {
			MistAPI.extensions().process(ctx, extensionId);

			if (ctx.result() == ChatResult.CANCEL) {
				return;
			}
		}
	}
}