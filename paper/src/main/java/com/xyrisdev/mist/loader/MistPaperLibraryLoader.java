package com.xyrisdev.mist.loader;

import com.google.gson.Gson;
import com.xyrisdev.mist.loader.internal.PluginLibraries;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class MistPaperLibraryLoader implements PluginLoader {

	private static final String LIBRARIES_RESOURCE = "/paper-libraries.json";
	private static final Gson GSON = new Gson();

	@Override
	public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
		final MavenLibraryResolver resolver = new MavenLibraryResolver();
		final PluginLibraries libraries = load();

		libraries.asDependencies().forEach(resolver::addDependency);
		libraries.asRepositories().forEach(resolver::addRepository);

		classpathBuilder.addLibrary(resolver);
	}

	private PluginLibraries load() {
		final InputStream stream = Objects.requireNonNull(
				getClass().getResourceAsStream(LIBRARIES_RESOURCE),
				LIBRARIES_RESOURCE + " not found on classpath"
		);

		try (stream) {
			return GSON.fromJson(
					new InputStreamReader(stream, StandardCharsets.UTF_8),
					PluginLibraries.class
			);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load Paper libraries", e);
		}
	}
}
