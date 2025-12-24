package com.xyrisdev.mist.extension.format.entry;

import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

public record ClickAction(
		@NotNull ClickEvent.Action action,
		@NotNull String value
) {}
