package com.xyrisdev.mist.util.build;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Properties;

public class BuildInformation {

	private static final BuildInformation INSTANCE = load();

	private final String module;
	private final String version;
	private final String branch;
	private final String commit;
	private final String commitShort;

	private BuildInformation(
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

	public static @NotNull BuildInformation instance() {
		return INSTANCE;
	}

	@Contract(" -> new")
	private static @NotNull BuildInformation load() {
		final Properties prop = new Properties();

		try (InputStream insteam = BuildInformation.class.getClassLoader().getResourceAsStream("build.properties")) {
			if (insteam != null) {
				prop.load(insteam);
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

	public String module() {
		return module;
	}

	public String version() {
		return version;
	}

	public String branch() {
		return branch;
	}

	public String commit() {
		return commit;
	}

	public String commitShort() {
		return commitShort;
	}
}
