# Welcome to MCWandsFramework!
This plugin provides an easy-to-use framework which allows developers to create their own custom wands in Minecraft!
MCWandsFramework provides the implementation details for the following:
* Commands to create new wands
* Basic permissions
* Tab-completion
* Normal shooting mechanics

The only thing required from users creating custom wands is how the wand will behave. That's it!
# Server Installation
### Requirements:
* Java 16+ for Minecraft 1.17+.
* Use Spigot, Paper, or any fork of those.
### Installation Instructions:
* Drag MCWandsFramework into plugins folder.
* Start server.
* That's it! OP users are given all permissions by default. For those wishing to give permissions to non-OP users, permissions can be found HERE!!!.

# Developer Instructions:
Adding wands to MCWandsFramework is simple, whether it be in Java or Kotlin!

## Before You Begin:
* MCWandsFramework is compiled to Java 16 for Minecraft 1.17+. Custom wands should meet or exceed this specification.

## Base Dependency Information:
Gradle (Groovy DSL)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.HoshiKurama:MCWandsFramework:5.0.0'
}
```
Gradle (Kotlin DSL)
```kotlin
repositories { 
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.HoshiKurama:MCWandsFramework:5.0.0")
}
```
Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.HoshiKurama</groupId>
    <artifactId>MCWandsFramework</artifactId>
    <version>5.0.0</version>
</dependency>
```

## Further Instructions:
### Kotlin users should continue here.
### Java users should continue here.
