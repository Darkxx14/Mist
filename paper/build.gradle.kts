import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    java
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

val branch = BuildInformation.branch()
val module = BuildInformation.module(project)
val version = BuildInformation.version(project)

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
    archiveVersion.set(version)
    archiveClassifier.set(branch)
}

tasks.shadowJar {
    archiveClassifier.set(branch)
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
    inputs.properties(
        mapOf(
            "module" to module,
            "version" to version,
            "branch" to branch
        )
    )

    filesMatching("**/build.properties") {
        expand(
            "module" to module,
            "version" to version,
            "branch" to branch
        )
    }
}

paper {
    name = "Mist"
    version = project.version.toString() + "-" + branch
    apiVersion = "1.21"

    main = "com.xyrisdev.mist.MistPaperPlugin"
    bootstrapper = "com.xyrisdev.mist.loader.MistPaperBootstrapper"
    loader = "com.xyrisdev.mist.loader.MistPaperLibraryLoader"

    generateLibrariesJson = true
    foliaSupported = true
    authors = listOf("XyrisDevelopment", "Darkxx")

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
                "mist.commands.reload",
                "mist.commands.broadcast",
                "mist.commands.similarity",
                "mist.commands.chat",
                "mist.bypass.chat"
            )
        }

        register("mist.commands.reload") {
            description = "Reload Mist configuration"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.commands.broadcast") {
            description = "Broadcast"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.commands.similarity") {
            description = "Similarity"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.commands.chat") {
            description = "Chat"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.bypass.chat") {
            description = "Bypass Chat Lock"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

tasks.generatePaperPluginDescription {
    useDefaultCentralProxy()
}
