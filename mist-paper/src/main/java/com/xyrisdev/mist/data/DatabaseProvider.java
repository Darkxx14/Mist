package com.xyrisdev.mist.data;

import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;

public interface DatabaseProvider {

	void connect();
	void shutdown();
	ChatUserRepository users();

}
