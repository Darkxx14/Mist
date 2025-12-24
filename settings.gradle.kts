rootProject.name = "Mist"

include(
        "api",
        "paper"
)

pluginManagement {
        repositories {
                gradlePluginPortal()
                mavenCentral()
                maven("https://repo.papermc.io/repository/maven-public/")
        }
}

plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}