import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.github.weave-mc.weave-gradle") version "649dba7468"
    application
}

group = "club.maxstats.seraph"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    mavenCentral()
}

val runtime: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(runtime)
runtime.isTransitive = false

dependencies {
    runtime("com.github.exejar:HyKo:9c840f932e")
    runtime("com.github.exejar:Kolour:e5a4dc8bb6")
    runtime("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    compileOnly("com.github.weave-mc:weave-loader:70bd82faa6")
    compileOnly("org.spongepowered:mixin:0.8.5")
}

minecraft.version("1.8.9")

application {
    mainClass.set("club.maxstats.seraph.Main")
}

tasks {
    withType<ShadowJar> {
        configurations = listOf(runtime)
        destinationDirectory.set(file("${System.getProperty("user.home")}/.weave/mods"))
    }
    named<Jar>("jar") {
        enabled = false
    }
    build {
        dependsOn("shadowJar")
    }
}