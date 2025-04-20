pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") // FabricMC
        maven("https://maven.minecraftforge.net/") // MinecraftForge
        maven("https://maven.neoforged.net/releases/") // NeoForge
        maven("https://maven.kikugie.dev/releases") // Stonecutter
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.5"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(rootProject) {
        versions("1.20.1", "1.20.4", "1.20.6", "1.21.1", "1.21.5")

        branch("fabric")
        branch("neoforge") {
            versions("1.20.4", "1.20.6", "1.21.1", "1.21.5")
        }
    }
}

rootProject.name = "ImGui for MC"