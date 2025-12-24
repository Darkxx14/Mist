package com.xyrisdev.mist.loader.internal;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public record PluginLibraries(
		@NotNull Map<String, String> repositories,
		@NotNull List<String> dependencies
) {

	public Stream<Dependency> asDependencies() {
		return dependencies
				.stream()
				.map(d -> new Dependency(new DefaultArtifact(d), null));
	}

	public Stream<RemoteRepository> asRepositories() {
		return repositories.entrySet()
				.stream()
				.map(e -> new RemoteRepository.Builder(
						e.getKey(),
						"default",
						e.getValue()
				).build());
	}
}
