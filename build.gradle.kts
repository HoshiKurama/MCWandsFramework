import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    java
    `maven-publish`
}

group = "com.hoshikurama.github"
version = "4.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib", version = "1.5.10"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")
}

tasks.withType<KotlinCompile>() {
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