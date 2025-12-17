@file:Suppress("UnstableApiUsage")

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
