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

public record SQLiteChatUserRepository(
		@NotNull DataSource dataSource,
		@NotNull String table
) implements ChatUserRepository {

	@Override
	public @NotNull ChatUser load(@NotNull UUID id) {
		try (Connection conn = dataSource.getConnection();
		     final PreparedStatement ps = conn.prepareStatement(
				     "SELECT data FROM " + table + " WHERE uuid = ?"
		     )) {

			ps.setString(1, id.toString());

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return ChatUserSerializer.deserialize(
							id,
							rs.getString("data")
					);
				}
			}

			return new ChatUser(id);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load ChatUser " + id, e);
		}
	}

	@Override
	public void save(@NotNull ChatUser user) {
		try (Connection conn = dataSource.getConnection();
		     final PreparedStatement ps = conn.prepareStatement(
				     """
						     INSERT INTO %s (uuid, data)
						     VALUES (?, ?)
						     ON CONFLICT(uuid) DO UPDATE SET data = excluded.data
						     """.formatted(table)
		     )) {

			ps.setString(1, user.id().toString());
			ps.setString(2, ChatUserSerializer.serialize(user));
			ps.executeUpdate();

		} catch (Exception e) {
			throw new IllegalStateException("Failed to save ChatUser " + user.id(), e);
		}
	}

	@Override
	public void delete(@NotNull UUID id) {
		try (Connection conn = dataSource.getConnection();
		     PreparedStatement ps = conn.prepareStatement(
				     "DELETE FROM " + table + " WHERE uuid = ?"
		     )) {

			ps.setString(1, id.toString());
			ps.executeUpdate();

		} catch (Exception e) {
			throw new IllegalStateException("Failed to delete ChatUser " + id, e);
		}
	}
}
