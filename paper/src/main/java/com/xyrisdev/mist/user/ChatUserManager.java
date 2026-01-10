package com.xyrisdev.mist.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;
import com.xyrisdev.mist.util.thread.MistExecutors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ChatUserManager {

	private final ChatUserRepository repository;

	private final Cache<@NotNull UUID, ChatUser> cache;
	private final Set<UUID> dirtyUsers = ConcurrentHashMap.newKeySet();

	public ChatUserManager(@NotNull ChatUserRepository repository) {
		this.repository = repository;
		this.cache = Caffeine.newBuilder()
				.removalListener((UUID id, ChatUser user, RemovalCause cause) -> {
					if (!this.dirtyUsers.remove(id)) {
						return;
					}

					MistExecutors.io().execute(() -> repository.save(user));
				})
				.build();
	}

	public CompletableFuture<ChatUser> load(@NotNull UUID id) {
		final ChatUser cached = this.cache.getIfPresent(id);

		if (cached != null) {
			return CompletableFuture.completedFuture(cached);
		}

		final CompletableFuture<ChatUser> future = new CompletableFuture<>();

		MistExecutors.io().execute(() -> {
			try {
				final ChatUser user = this.repository.load(id);

				this.cache.put(id, user);
				future.complete(user);
			} catch (Throwable t) {
				future.completeExceptionally(t);
			}
		});

		return future;
	}

	public ChatUser get(@NotNull UUID id) {
		return this.cache.getIfPresent(id);
	}

	public void modify(@NotNull Player player, @NotNull Consumer<ChatUser> action) {
		this.modify(player.getUniqueId(), action);
	}

	public void modify(@NotNull UUID id, @NotNull Consumer<ChatUser> action) {
		final ChatUser user = this.cache.getIfPresent(id);

		if (user == null) {
			return;
		}

		action.accept(user);
		this.dirtyUsers.add(id);
	}

	public void save(@NotNull UUID id) {
		final ChatUser user = this.cache.getIfPresent(id);

		if (user == null || !this.dirtyUsers.remove(id)) {
			return;
		}

		MistExecutors.io().execute(() -> this.repository.save(user));
	}

	public void flush() {
		if (this.dirtyUsers.isEmpty()) {
			return;
		}

		MistExecutors.io().execute(() -> {
			for (UUID id : this.dirtyUsers) {
				final ChatUser user = this.cache.getIfPresent(id);

				if (user != null) {
					this.repository.save(user);
				}

			}

			dirtyUsers.clear();
		});
	}

	public void invalidate(@NotNull UUID id) {
		this.cache.invalidate(id);
	}

	public void invalidateAll() {
		this.cache.invalidateAll();
	}
}
