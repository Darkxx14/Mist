package com.xyrisdev.mist.data.user.sql.repository;

import com.xyrisdev.mist.api.chat.user.ChatUser;
import com.xyrisdev.mist.api.chat.user.data.serialize.ChatUserSerializer;
import com.xyrisdev.mist.api.chat.user.repository.ChatUserRepository;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class H2ChatUserRepository implements ChatUserRepository {

	private final DataSource dataSource;
	private final String table;

	public H2ChatUserRepository(@NotNull DataSource dataSource, @NotNull String table) {
		this.dataSource = dataSource;
		this.table = table;
	}

	@Override
	public @NotNull CompletableFuture<ChatUser> load(@NotNull UUID uuid) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection conn = dataSource.getConnection();
			     PreparedStatement ps = conn.prepareStatement(
					     "SELECT data FROM " + table + " WHERE uuid = ?"
			     )) {

				ps.setString(1, uuid.toString());

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return ChatUserSerializer.deserialize(
								uuid,
								rs.getString("data")
						);
					}
				}

				return new ChatUser(uuid);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to load ChatUser " + uuid, e);
			}
		});
	}

	@Override
	public @NotNull CompletableFuture<Void> save(@NotNull ChatUser user) {
		return CompletableFuture.runAsync(() -> {
			try (Connection conn = dataSource.getConnection();
			     PreparedStatement ps = conn.prepareStatement(
					     """
                         MERGE INTO %s (uuid, data)
                         KEY (uuid)
                         VALUES (?, ?)
                         """.formatted(table)
			     )) {

				ps.setString(1, user.id().toString());
				ps.setString(2, ChatUserSerializer.serialize(user));
				ps.executeUpdate();

			} catch (Exception e) {
				throw new IllegalStateException("Failed to save ChatUser " + user.id(), e);
			}
		});
	}

	@Override
	public @NotNull CompletableFuture<Void> delete(@NotNull UUID uuid) {
		return CompletableFuture.runAsync(() -> {
			try (Connection conn = dataSource.getConnection();
			     PreparedStatement ps = conn.prepareStatement(
					     "DELETE FROM " + table + " WHERE uuid = ?"
			     )) {

				ps.setString(1, uuid.toString());
				ps.executeUpdate();

			} catch (Exception e) {
				throw new IllegalStateException("Failed to delete ChatUser " + uuid, e);
			}
		});
	}
}
