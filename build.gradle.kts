plugins {
    kotlin("jvm") version "1.8.20"
    id("com.github.weave-mc.weave-gradle") version "823628f548"
    application
}

group = "club.maxstats.weavetabstats"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    mavenCentral()
}

dependencies {
    implementation("com.github.exejar:HyKo:79365e92b3")
    compileOnly("com.github.weave-mc:weave-loader:c8c6186117")
    compileOnly("org.spongepowered:mixin:0.8.5")
}

minecraft.version("1.8.9")

tasks.jar {
    destinationDirectory.set(file("${System.getProperty("user.home")}/.lunarclient/mods"))
    archiveBaseName.set("WeaveTabStats")
}