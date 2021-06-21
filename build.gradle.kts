import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
    `java-library`
    `maven-publish`
}

application {
    mainClass.set("com.hoshikurama.github.mcwandsframework.MCWandsFramework")
}

group = "com.hoshikurama.github"
version = "4.0.3"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
    api(kotlin("stdlib", version = "1.5.10"))
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        dependencies {
            include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0"))
            include(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0"))
            include(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0"))
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib:1.5.10"))
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

// Publishing Instructions
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENSE.md") {
        into("META-INF")
    }
}

publishing {
    publications {
        create<MavenPublication>("MCWandsFramework") {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}
