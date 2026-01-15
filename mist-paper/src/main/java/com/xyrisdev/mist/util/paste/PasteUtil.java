package com.xyrisdev.mist.util.paste;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xyrisdev.mist.util.paste.response.PasteResponse;
import com.xyrisdev.mist.util.thread.MistExecutors;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class PasteUtil {

	private static final URI API_ENDPOINT = URI.create("https://api.pastes.dev/post");
	private static final String VIEW_BASE = "https://pastes.dev/";

	private static final HttpClient CLIENT = HttpClient.newBuilder()
											.executor(MistExecutors.processor())
											.build();

	private static final Gson GSON = new GsonBuilder()
									.disableHtmlEscaping()
									.setPrettyPrinting()
									.create();

	public static @NotNull CompletableFuture<String> paste(@NotNull Object value) {
		final String json = GSON.toJson(value);

		final HttpRequest request = HttpRequest.newBuilder(API_ENDPOINT)
				.header("Content-Type", "text/json")
				.header("User-Agent", "Mist (github.com/Darkxx14/Mist)")
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.build();

		return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(response -> {
					if (response.statusCode() != 200 && response.statusCode() != 201) {
						throw new IllegalStateException(
								"Paste failed (" + response.statusCode() + "): " + response.body()
						);
					}

					return VIEW_BASE + GSON
							.fromJson(response.body(), PasteResponse.class)
							.key();
				});
	}
}
