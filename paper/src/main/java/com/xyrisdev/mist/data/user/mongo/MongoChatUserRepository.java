package com.xyrisdev.mist.data.user.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.data.serialize.ChatUserSerializer;
import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Filters.eq;

public class MongoChatUserRepository implements ChatUserRepository {

	private final MongoCollection<Document> collection;

	public MongoChatUserRepository(@NotNull MongoCollection<Document> collection) {
		this.collection = collection;
	}

	@Override
	public @NotNull CompletableFuture<ChatUser> load(@NotNull UUID uuid) {
		return CompletableFuture.supplyAsync(() -> {
			final Document doc = collection.find(eq("_id", uuid.toString())).first();

			if (doc == null) {
				final ChatUser user = new ChatUser(uuid);

				save(user);
				return user;
			}

			final Document data = doc.get("data", Document.class);

			return ChatUserSerializer.deserialize(
					uuid,
					data.toJson()
			);
		});
	}

	@Override
	public @NotNull CompletableFuture<Void> save(@NotNull ChatUser user) {
		return CompletableFuture.runAsync(() -> {
			final Document doc = new Document("_id", user.id().toString())
					.append("data", Document.parse(
							ChatUserSerializer.serialize(user)
					));

			collection.replaceOne(
					eq("_id", user.id().toString()),
					doc,
					new ReplaceOptions().upsert(true)
			);
		});
	}

	@Override
	public @NotNull CompletableFuture<Void> delete(@NotNull UUID uuid) {
		return CompletableFuture.runAsync(() -> collection.deleteOne(eq("_id", uuid.toString()))
		);
	}
}
