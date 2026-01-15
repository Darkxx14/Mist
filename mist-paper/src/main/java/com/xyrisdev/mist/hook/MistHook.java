package com.xyrisdev.mist.hook;

import com.xyrisdev.mist.util.logger.MistLogger;
import lombok.Builder;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Builder
@Accessors(fluent = true)
public record MistHook(
		@NotNull String plugin, @NotNull Consumer<@NotNull Plugin> onLoad,
		@Nullable Runnable onFail, boolean log,
		@Nullable String success, @Nullable String failure
) {

	public void register() {
		final Plugin target = Bukkit.getPluginManager().getPlugin(plugin);

		if (target != null && target.isEnabled()) {
			if (log && success != null) {
				MistLogger.info(success);
			}

			onLoad.accept(target);
			return;
		}

		if (log && failure != null) {
			MistLogger.info(failure);
		}

		if (onFail != null) {
			onFail.run();
		}
	}
}
