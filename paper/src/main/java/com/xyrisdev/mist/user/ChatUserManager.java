package com.xyrisdev.mist.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

@SuppressWarnings("CallToPrintStackTrace")
public class ChatUserManager {

	private final ChatUserRepository repository;
	private final Cache<UUID, ChatUser> cache;

	private final Set<UUID> dirtyUsers = ConcurrentHashMap.newKeySet();
	private final ScheduledExecutorService exe;

	public ChatUserManager(@NotNull ChatUserRepository repository) {
		this.repository = repository;

		this.cache = Caffeine.newBuilder()
				.removalListener(this::onRemove)
				.build();

		this.exe = Executors.newSingleThreadScheduledExecutor(r -> {
			final Thread thread = new Thread(r, "mist-user-flush");
			thread.setDaemon(true);
			return thread;
		});
	}

	private void onRemove(UUID id, ChatUser user, RemovalCause cause) {
		if (user == null) {
			return;
		}

		if (dirtyUsers.remove(id)) {
			repository.save(user);
		}
	}

	public CompletableFuture<ChatUser> load(@NotNull UUID id) {
		final ChatUser cached = cache.getIfPresent(id);

		if (cached != null) {
			return CompletableFuture.completedFuture(cached);
		}

		return repository.load(id).thenApply(user -> {
			cache.put(id, user);
			return user;
		});
	}

	public ChatUser get(@NotNull UUID id) {
		return cache.getIfPresent(id);
	}

	public void modify(@NotNull UUID id, @NotNull Consumer<ChatUser> action) {
		final ChatUser user = cache.getIfPresent(id);
		if (user == null) {
			return;
		}

		action.accept(user);
		dirtyUsers.add(id);
	}

	public CompletableFuture<Void> save(@NotNull UUID id) {
		final ChatUser user = cache.getIfPresent(id);

		if (user == null || !dirtyUsers.remove(id)) {
			return CompletableFuture.completedFuture(null);
		}

		return repository.save(user);
	}

	public CompletableFuture<Void> flush() {
		if (dirtyUsers.isEmpty()) {
			return CompletableFuture.completedFuture(null);
		}

		return CompletableFuture.allOf(
				dirtyUsers.stream()
						.map(uuid -> {
							final ChatUser user = cache.getIfPresent(uuid);

							if (user == null) {
								return CompletableFuture.completedFuture(null);
							}

							return repository.save(user);
						})
						.toArray(CompletableFuture[]::new)
		).thenRun(dirtyUsers::clear);
	}

	public void invalidate(@NotNull UUID id) {
		cache.invalidate(id);
	}

	public void invalidateAll() {
		cache.invalidateAll();
	}

	public void autoFlush(@NotNull Duration interval) {
		if (interval.isZero() || interval.isNegative()) {
			return;
		}

		final long ms = interval.toMillis();

		exe.scheduleAtFixedRate(() ->
						flush().exceptionally(ex -> {
							ex.printStackTrace();
							return null;
						}),
				ms,
				ms,
				TimeUnit.MILLISECONDS
		);
	}

	public void shutdown() {
		exe.shutdown();
		flush().join();
	}
}
