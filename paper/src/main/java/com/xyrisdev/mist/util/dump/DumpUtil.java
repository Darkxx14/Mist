package com.xyrisdev.mist.util.dump;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.MistPlugin;
import com.xyrisdev.mist.util.logger.MistLogger;
import com.xyrisdev.mist.util.paste.PasteUtil;
import io.papermc.paper.plugin.configuration.PluginMeta;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.lang.management.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class DumpUtil {

	private static final long MAX_CONFIG_SIZE = 512L * 1024L;

	public static @NotNull CompletableFuture<String> dump() {
		return PasteUtil.paste(build());
	}

	@Contract(" -> new")
	private static @NotNull Dump build() {
		final Runtime runtime = Runtime.getRuntime();

		final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();

		final List<PluginInfo> plugins = new ArrayList<>();

		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			plugins.add(pl(plugin));
		}

		return new Dump(
				System.currentTimeMillis(),
				serverInfo(),
				systemInfo(osMxBean),
				javaInfo(runtimeMXBean),
				memInfo(runtime, memoryMXBean),
				schedulerInfo(Bukkit.getScheduler()),
				pl(Mist.INSTANCE.plugin()),
				configs(),
				plugins
		);
	}

	@Contract(" -> new")
	private static @NotNull ServerInfo serverInfo() {
		return new ServerInfo(
				Bukkit.getName(),
				Bukkit.getVersion(),
				Bukkit.getBukkitVersion(),
				Bukkit.getMinecraftVersion(),
				Bukkit.getOnlineMode(),
				Bukkit.getMaxPlayers(),
				Bukkit.getOnlinePlayers().size()
		);
	}

	@Contract("_ -> new")
	private static @NotNull SystemInfo systemInfo(@NotNull OperatingSystemMXBean os) {
		return new SystemInfo(
				os.getName(),
				os.getArch(),
				os.getVersion(),
				os.getAvailableProcessors()
		);
	}

	@Contract("_ -> new")
	private static @NotNull JavaInfo javaInfo(@NotNull RuntimeMXBean runtime) {
		return new JavaInfo(
				System.getProperty("java.version"),
				System.getProperty("java.vendor"),
				System.getProperty("java.vm.name"),
				System.getProperty("java.vm.version"),
				runtime.getInputArguments(),
				runtime.getUptime()
		);
	}

	private static @NotNull MemoryInfo memInfo(@NotNull Runtime runtime, @NotNull MemoryMXBean memory) {
		return new MemoryInfo(
				mem(runtime.maxMemory()),
				mem(runtime.totalMemory()),
				mem(runtime.freeMemory()),
				mem(memory.getHeapMemoryUsage().getUsed()),
				mem(memory.getNonHeapMemoryUsage().getUsed())
		);
	}

	private static @NotNull SchedulerInfo schedulerInfo(@NotNull BukkitScheduler scheduler) {
		final List<TaskInfo> tasks = new ArrayList<>();

		for (BukkitTask task : scheduler.getPendingTasks()) {
			tasks.add(new TaskInfo(
					task.getTaskId(),
					task.getOwner().getName(),
					task.isSync(),
					task.isCancelled()
			));
		}

		return new SchedulerInfo(
				scheduler.getPendingTasks().size(),
				scheduler.getActiveWorkers().size(),
				tasks
		);
	}

	private static @NotNull PluginInfo pl(@NotNull Plugin plugin) {
		final PluginMeta meta = plugin.getPluginMeta();

		return new PluginInfo(
				meta.getName(),
				meta.getVersion(),
				plugin.isEnabled(),
				meta.getMainClass(),
				meta.getLoadOrder().name(),
				meta.getAPIVersion(),
				meta.getAuthors(),
				meta.getContributors(),
				meta.getPluginDependencies(),
				meta.getPluginSoftDependencies(),
				meta.getLoadBeforePlugins(),
				meta.getProvidedPlugins(),
				meta.getWebsite(),
				meta.getDescription(),
				meta.getPermissionDefault().name(),
				meta.getPermissions()
						.stream()
						.map(Permission::getName)
						.toList()
		);
	}

	private static @NotNull Map<String, Object> configs() {
		final Map<String, Object> output = new LinkedHashMap<>();
		final Path root = Mist.INSTANCE.plugin().getDataFolder().toPath();
		final Yaml yaml = new Yaml();

		if (!Files.exists(root)) {
			log("data folder missing, skipping config dump");
			return output;
		}

		try {
			Files.walk(root)
					.filter(Files::isRegularFile)
					.filter(f -> {
						final String n = f.getFileName().toString().toLowerCase(Locale.ROOT);
						return n.endsWith(".yml") || n.endsWith(".yaml");
					})
					.forEach(file -> {
						final String relative = root.relativize(file)
								.toString()
								.replace(File.separatorChar, '/');

						try {
							final long size = Files.size(file);

							if (size > MAX_CONFIG_SIZE) {
								output.put(relative, "skipped (too large)");
								return;
							}

							final String raw = Files.readString(file);

							output.put(relative, sanitize(yaml.load(raw)));
						} catch (Exception e) {
							output.put(relative, "error");
							log("failed to read config " + relative + ": " + e.getMessage());
						}
					});
		} catch (Exception e) {
			log("config scan failed: " + e.getMessage());
		}

		return output;
	}

	private static Object sanitize(Object value) {
		if (value instanceof Map<?, ?> map) {
			final Map<String, Object> cleaned = new LinkedHashMap<>();

			for (Map.Entry<?, ?> entry : map.entrySet()) {
				final String key = String.valueOf(entry.getKey());

				if (isSensitive(key)) {
					cleaned.put(key, "<redacted>");
				} else {
					cleaned.put(key, sanitize(entry.getValue()));
				}
			}

			return cleaned;
		}

		if (value instanceof List<?> list) {
			final List<Object> out = new ArrayList<>(list.size());

			for (Object o : list) {
				out.add(sanitize(o));
			}

			return out;
		}

		return value;
	}

	private static boolean isSensitive(@NotNull String key) {
		final String k = key.toLowerCase(Locale.ROOT);

		return k.contains("password")
				|| k.contains("username")
				|| k.contains("host")
				|| k.contains("uri")
				|| k.equals("mysql")
				|| k.equals("mongo")
				|| k.equals("sqlite")
				|| k.equals("h2");
	}

	private static @NotNull MemoryValue mem(long bytes) {
		return new MemoryValue(bytes, prettyBytes(bytes));
	}

	private static @NotNull String prettyBytes(long bytes) {
		if (bytes < 1024) {
			return bytes + " B";
		}

		final int unit = 1024;
		final int exp = (int) (Math.log(bytes) / Math.log(unit));
		return String.format(
				"%.2f %sB",
				bytes / Math.pow(unit, exp),
				"KMGTPE".charAt(exp - 1)
		);
	}

	private static void log(String msg) {
		MistLogger.info("[dump] " + msg);
	}

	private record Dump(
			long timestamp,
			ServerInfo server,
			SystemInfo system,
			JavaInfo java,
			MemoryInfo memory,
			SchedulerInfo scheduler,
			PluginInfo self,
			Map<String, Object> mistConfigs,
			List<PluginInfo> plugins
	) {}

	private record ServerInfo(
			String platform,
			String version,
			String bukkitVersion,
			String minecraftVersion,
			boolean onlineMode,
			int maxPlayers,
			int onlinePlayers
	) {}

	private record SystemInfo(
			String os,
			String arch,
			String osVersion,
			int processors
	) {}

	private record JavaInfo(
			String version,
			String vendor,
			String vmName,
			String vmVersion,
			List<String> jvmArgs,
			long uptimeMillis
	) {}

	private record MemoryInfo(
			MemoryValue max,
			MemoryValue total,
			MemoryValue free,
			MemoryValue heapUsed,
			MemoryValue nonHeapUsed
	) {}

	private record MemoryValue(long bytes, String formatted) {}

	private record SchedulerInfo(
			int pendingTasks,
			int activeWorkers,
			List<TaskInfo> tasks
	) {}

	private record TaskInfo(
			int id,
			String owner,
			boolean sync,
			boolean cancelled
	) {}

	private record PluginInfo(
			String name,
			String version,
			boolean enabled,
			String mainClass,
			String loadOrder,
			String apiVersion,
			List<String> authors,
			List<String> contributors,
			List<String> dependencies,
			List<String> softDependencies,
			List<String> loadBefore,
			List<String> provides,
			String website,
			String description,
			String permissionDefault,
			List<String> permissions
	) {}
}
