package com.xyrisdev.mist.api.chat.user.repository;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface ChatUserRepository {

	@NotNull ChatUser load(@NotNull UUID id);
	void save(@NotNull ChatUser user);
	void delete(@NotNull UUID id);
}