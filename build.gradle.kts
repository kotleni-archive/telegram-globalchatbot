import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "me.kotleni"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.elbekD:kt-telegram-bot:+")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("bot")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "Main"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}