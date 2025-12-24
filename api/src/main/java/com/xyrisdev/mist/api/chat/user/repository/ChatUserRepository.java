package com.xyrisdev.mist.api.chat.user.repository;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ChatUserRepository {

	@NotNull CompletableFuture<ChatUser> load(@NotNull UUID uuid);

	@NotNull CompletableFuture<Void> save(@NotNull ChatUser user);

	@NotNull CompletableFuture<Void> delete(@NotNull UUID uuid);
}
