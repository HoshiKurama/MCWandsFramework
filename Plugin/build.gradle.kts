plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
    `java-library`
}

application {
    mainClass.set("com.github.hoshikurama.mcwandsframework.MCWandsFramework")
}

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
    api(kotlin("stdlib"))
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")
    implementation(project(":API"))
}

tasks {
    shadowJar {
        dependencies {
            include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0"))
            include(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0"))
            include(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0"))
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib:1.5.30"))
            include(project(":API"))

            relocate("kotlin", "com.github.hoshikurama.mcwandsframework.shaded.kotlin")
            relocate("kotlinx.coroutines", "com.github.hoshikurama.mcwandsframework.shaded.kotlinx.coroutines")
            relocate("com.github.shynixn.mccoroutine", "com.github.hoshikurama.mcwandsframework.shaded.com.github.shynixn.mccoroutine")
        }
    }
}