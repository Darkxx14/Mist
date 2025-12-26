package com.xyrisdev.mist.api.chat.user.ignore;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatIgnore {

	private final Set<UUID> ignored = ConcurrentHashMap.newKeySet();

	public boolean contains(@NotNull UUID id) {
		return ignored.contains(id);
	}

	public void add(@NotNull UUID id) {
		ignored.add(id);
	}

	public void remove(@NotNull UUID id) {
		ignored.remove(id);
	}

	@Contract(pure = true)
	public @NotNull @Unmodifiable Set<UUID> snapshot() {
		return Set.copyOf(ignored);
	}
}
