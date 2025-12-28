package com.xyrisdev.mist.data.user.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.data.serialize.ChatUserSerializer;
import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MongoChatUserRepository implements ChatUserRepository {

	private final MongoCollection<Document> collection;

	public MongoChatUserRepository(@NotNull MongoCollection<Document> collection) {
		this.collection = collection;
	}

	@Override
	public @NotNull ChatUser load(@NotNull UUID id) {
		final Document doc = collection.find(Filters.eq("_id", id.toString())).first();

		if (doc == null) {
			final ChatUser user = new ChatUser(id);

			save(user);
			return user;
		}

		final Document data = doc.get("data", Document.class);

		return ChatUserSerializer.deserialize(id, data.toJson());
	}

	@Override
	public void save(@NotNull ChatUser user) {
		final Document doc = new Document("_id", user.id().toString())
				.append("data", Document.parse(
						ChatUserSerializer.serialize(user)
				));

		collection.replaceOne(
				Filters.eq("_id", user.id().toString()),
				doc,
				new ReplaceOptions().upsert(true)
		);
	}

	@Override
	public void delete(@NotNull UUID id) {
		collection.deleteOne(Filters.eq("_id", id.toString()));
	}
}
