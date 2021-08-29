import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
    `java-library`
    `maven-publish`
}

application {
    mainClass.set("com.github.hoshikurama.mcwandsframework.MCWandsFramework")
}

group = "com.github.hoshikurama"
version = "5.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
    api(kotlin("stdlib", version = "1.5.30"))
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")
}

tasks {
    shadowJar {
        dependencies {
            include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0"))
            include(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0"))
            include(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0"))
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib:1.5.30"))

            // THIS GETS COMMENTED OUT DURING BUILD FOR API CREATION
            relocate("kotlin", "com.github.hoshikurama.mcwandsframework.shaded.kotlin")
            relocate("kotlinx.coroutines", "com.github.hoshikurama.mcwandsframework.shaded.kotlinx.coroutines")
            relocate("com.github.shynixn.mccoroutine", "com.github.hoshikurama.mcwandsframework.shaded.com.github.shynixn.mccoroutine")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
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
