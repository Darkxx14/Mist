package com.xyrisdev.mist.util.build;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Properties;

public record BuildInformation(String module, String version, String branch, String commit, String commitShort) {

	private static final BuildInformation INSTANCE = load();

	public BuildInformation(
			@NotNull String module, @NotNull String version,
			@NotNull String branch, @NotNull String commit,
			@NotNull String commitShort
	) {
		this.module = module;
		this.version = version;
		this.branch = branch;
		this.commit = commit;
		this.commitShort = commitShort;
	}

	public static @NotNull BuildInformation of() {
		return INSTANCE;
	}

	@Contract(" -> new")
	private static @NotNull BuildInformation load() {
		final Properties prop = new Properties();

		try (InputStream instream = BuildInformation.class.getClassLoader().getResourceAsStream("build.properties")) {
			if (instream != null) {
				prop.load(instream);
			}
		} catch (Exception ignored) {}

		return new BuildInformation(
				prop.getProperty("module", "unknown"),
				prop.getProperty("version", "unknown"),
				prop.getProperty("branch", "unknown"),
				prop.getProperty("commit", "unknown"),
				prop.getProperty("commit_short", "unknown")
		);
	}
}
