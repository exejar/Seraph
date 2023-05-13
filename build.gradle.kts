plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.0"
    id("com.github.weave-mc.weave-gradle") version "823628f548"
    application
}

group = "club.maxstats.seraph"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    implementation("com.github.exejar:HyKo:79365e92b3")
    compileOnly("com.github.weave-mc:weave-loader:c8c6186117")
    compileOnly("org.spongepowered:mixin:0.8.5")
}

minecraft.version("1.8.9")

tasks.jar {
    destinationDirectory.set(file("${System.getProperty("user.home")}/.lunarclient/mods"))
    archiveBaseName.set("Seraph")
}