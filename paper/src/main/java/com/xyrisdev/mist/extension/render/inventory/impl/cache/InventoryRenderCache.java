package com.xyrisdev.mist.extension.render.inventory.impl.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.extension.render.inventory.impl.object.InventoryRenderEntry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryRenderCache {

	private static final int MAX_ENTRIES = 5;

	private final Cache<@NotNull UUID, InventoryRenderEntry> cache = Caffeine.newBuilder()
							   .expireAfterWrite(Duration.ofMinutes(10))
							   .build();

	private final Map<Player, Deque<UUID>> index = new ConcurrentHashMap<>();

	public void put(@NotNull InventoryRenderEntry entry) {
		final Deque<UUID> deque = index.computeIfAbsent(entry.owner(), k -> new ArrayDeque<>());

		if (deque.size() >= MAX_ENTRIES) {
			final UUID evicted = deque.pollFirst();

			if (evicted != null) {
				cache.invalidate(evicted);
			}
		}

		deque.addLast(entry.id());
		cache.put(entry.id(), entry);
	}

	public InventoryRenderEntry get(@NotNull UUID id) {
		return cache.getIfPresent(id);
	}

	public void invalidate(@NotNull Player player) {
		final Deque<UUID> renders = index.remove(player);

		if (renders == null) {
			return;
		}

		for (UUID id : renders) {
			cache.invalidate(id);
		}
	}
}
