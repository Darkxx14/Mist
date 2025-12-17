import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    java
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
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
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

base {
    archivesName.set("mist-paper")
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveVersion.set(project.version.toString())
}

tasks.shadowJar {
    archiveClassifier.set("")

    relocate(
         "com.xyrisdev.library",
         "com.xyrisdev.mist.shaded.library"
    )
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

paper {
    name = "Mist"
    version = project.version.toString()
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
    }

    permissions {
        register("mist.admin") {
            description = "Full access to Mist"
            default = BukkitPluginDescription.Permission.Default.OP
            children = listOf(
                "mist.reload",
                "mist.broadcast"
            )
        }

        register("mist.reload") {
            description = "Reload Mist configuration"
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("mist.broadcast") {
            description = "Broadcast"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

tasks {
    generatePaperPluginDescription {
        useDefaultCentralProxy()
    }
}
