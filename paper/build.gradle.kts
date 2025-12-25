import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription
import xyz.jpenilla.runpaper.task.RunServer
import xyz.jpenilla.runtask.task.AbstractRun

plugins {
    java
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

val branch = BuildInformation.branch()
val module = BuildInformation.module(project)
val buildVersion = BuildInformation.version(project)

val commit = BuildInformation.commit()
val commitShort = BuildInformation.commitShort()

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.xyrisdev.com/repository/maven-public/")
    maven("https://repo.tcoded.com/releases")
}

dependencies {
    implementation(project(":api"))
    implementation(libs.xlibrary)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)

    compileOnly(libs.paper.api)
    compileOnly(libs.placeholder.api)
    compileOnly(libs.luckperms)

    paperLibrary(libs.caffeine)
    paperLibrary(libs.evo.inflector)
    paperLibrary(libs.cloud.paper)
    paperLibrary(libs.folia.lib)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveBaseName.set("mist-$module")
    archiveVersion.set(buildVersion)
    archiveClassifier.set(commitShort)
}

tasks.shadowJar {
    archiveClassifier.set(commitShort)
}

tasks.jar {
    enabled = false
}

artifacts {
    archives(tasks.shadowJar)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.processResources {
    filesMatching("**/build.properties") {
        expand(
            "module" to module,
            "version" to buildVersion,
            "branch" to branch,
            "commit" to commit,
            "commit_short" to commitShort
        )
    }
}

runPaper.folia.registerTask()

val plugins = runPaper.downloadPluginsSpec {
    modrinth("viaversion", "5.6.0")
    modrinth("viabackwards", "5.6.0")
    modrinth("placeholderapi", "2.11.7")
    url("https://ci.lucko.me/job/LuckPerms-Folia/lastBuild/artifact/bukkit/loader/build/libs/LuckPerms-Bukkit-5.5.11.jar")
}

tasks.withType<RunServer>().configureEach {
    minecraftVersion("1.21.8")
    downloadPlugins.from(plugins)
}

tasks.named<RunServer>("runServer") {
    runDirectory.set(rootProject.layout.projectDirectory.dir("servers/paper"))
}

tasks.named<RunServer>("runFolia") {
    runDirectory.set(rootProject.layout.projectDirectory.dir("servers/folia"))
}

@Suppress("UnstableApiUsage")
tasks.withType<AbstractRun>().configureEach {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }

    jvmArgs(
        "-XX:+AllowEnhancedClassRedefinition",
        "-Dcom.mojang.eula.agree=true",
        "-Dnet.kyori.ansi.colorLevel=truecolor"
    )
}

paper {
    name = "Mist"
    version = "$buildVersion-$commitShort"
    apiVersion = "1.21"

    main = "com.xyrisdev.mist.ChatPlugin"
    bootstrapper = "com.xyrisdev.mist.loader.MistPaperBootstrapper"
    loader = "com.xyrisdev.mist.loader.MistPaperLibraryLoader"

    foliaSupported = true
    generateLibrariesJson = true

    authors = listOf("darkxx16")

    serverDependencies {
        register("PlaceholderAPI") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = true
        }
        register("LuckPerms") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = true
        }
    }

    permissions {
        register("mist.admin") {
            description = "Full access to Mist"
            default = BukkitPluginDescription.Permission.Default.OP
            children = listOf(
                "mist.command",
                "mist.command.reload",
                "mist.command.broadcast",
                "mist.command.similarity",
                "mist.command.chat",
                "mist.command.regex",
                "mist.command.about",
                "mist.command.announcements",

                //bypass
                "mist.bypass.chat"
            )
        }

        register("mist.command") {
            description = "Access to /mist"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.command.reload") {
            description = "Reload Mist configuration"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.command.broadcast") {
            description = "Broadcast"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.command.similarity") {
            description = "Similarity"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.command.chat") {
            description = "Chat"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.command.regex") {
            description = "Regex"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.command.about") {
            description = "About"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.command.announcements") {
            description = "Announcements"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        // bypass
        register("mist.bypass.chat") {
            description = "Bypass Chat Lock"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

tasks.generatePaperPluginDescription {
    useDefaultCentralProxy()
}