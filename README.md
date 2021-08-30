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
* That's it! OP users are given all permissions by default. For those wishing to give permissions to non-OP users, permissions can be found [here](https://github.com/HoshiKurama/MCWandsFramework/wiki).

# Developers:
Adding wands to MCWandsFramework is simple, whether it be in Java or Kotlin!

## Instructions:
Before you begin, please note that MCWandsFramework assumes the following:
* Developers should refer to the server installation requirements section for determining the minimum Java version to use.
* Gradle is chosen build system. 
   * The Kotlin page uses the Kotlin DSL syntax.
   * The Java page uses the Groovy syntax.
* Users have a basic understanding of the build system and the chosen language.
* Examples are only meant to be a guide and may not explicitly state where the code is placed in your project.


Kotlin users should continue [here](https://github.com/HoshiKurama/MCWandsFramework/wiki/Kotlin-Developer-Instructions).  
Java users should continue [here](https://github.com/HoshiKurama/MCWandsFramework/wiki/Java-Developer-Instructions).
