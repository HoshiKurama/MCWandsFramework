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
* Java 11+ for Minecraft 1.16.
* Java 16+ for Minecraft 1.17+.
* Be using Paper or Spigot.
### Installation Instructions:
* Drag MCWandsFramework into plugins folder.
* Start server.
* That's it! OP users are given all permissions by default. For those wishing to give permissions to non-OP users,  permissions can be found HERE!!!.
# Developer Instructions
Adding wands to MCWandsFramework is simple, whether it be in Java or Kotlin!
## Requirements:
* MCWandsFramework is compiled to Java 11+ for Minecraft 1.16 and Java 16+ for Minecraft 1.17+. Custom wands should meet or exceed this specification.
* Kotlin users have access to some extra stuff that will be discussed in the Kotlin section.
## Dependency Information:
Groovy (build.gradle)
```groovy
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.HoshiKurama:MCWandsFramework:4.0.1'
}
```
Kotlin (build.gradle.kts)
```kotlin
repositories {
  maven { url = uri("https://jitpack.io") }
}

dependencies {
  implementation("com.github.HoshiKurama:MCWandsFramework:4.0.1")
}
```
Maven (pom.xml)
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependencies>
  <dependency>
    <groupId>com.github.HoshiKurama</groupId>
    <artifactId>MCWandsFramework</artifactId>
    <version>4.0.1</version>
  </dependency>
</dependencies>
```
## Registration
MCWandsFramework provides service through the ServicesManager API. Wand registration should only occur during or after onEnable is called. All registration must be performed on the main thread. Depending on the language used, registration can occur in the following ways:

Kotlin
```kotlin
// Nullable MCWands service acquired
val mcWandsService: MCWandsService? = Bukkit.getServicesManager().getRegistration(MCWandsService::class.java)?.provider  
 
 // Wand code may be defined at registration...
mcWandsService?.registerWandInKotlin("WandNameHere") { params, scope ->  
  /* Your code here! */
}

// ...or it may be first assigned to a variable!
val wandCode: KotlinParameterFunction = { params, scope ->    
  /* You code here */  
}  
mcWandsService?.registerWandInKotlin("WandNameHere", wandCode)
```
Java
```java
// Wands can be registered the classic way:  
RegisteredServiceProvider<MCWandsService> rsp = Bukkit.getServicesManager().getRegistration(MCWandsService.class);  
  
if (rsp != null) {  
  MCWandsService mcWandsService = rsp.getProvider();  
  // Wand code may be defined at registration...  
  mcWandsService.registerWandInJava("WandNameHere", params -> {/* Your code here */});  
  
  // ...or it may be first assigned to a variable!  
  Consumer<JavaWand> wandCode = params -> {/* Your code here */};  
  mcWandsService.registerWandInJava("WandNameHere", wandCode);  
}  
  
  
// Java optionals can be used instead!:  
Optional.ofNullable(Bukkit.getServicesManager().getRegistration(MCWandsService.class))
    .map(RegisteredServiceProvider::getProvider)  
    .ifPresent( service -> {  
      service.registerWandInJava("WandNameHere", params -> {  
      /* Your code here */ 
      });  
    });
```
NOTE: Please make sure to list MCWandsFramework as a dependency in your plugin.yml!
### Example Code:
Kotlin
```kotlin
import com.hoshikurama.github.mcwandsframework.MCWandsService  
import org.bukkit.Bukkit  
import org.bukkit.plugin.java.JavaPlugin  
  
class TestWand : JavaPlugin() {  
  
  override fun onEnable() {  
    Bukkit.getServicesManager().getRegistration(MCWandsService::class.java)  
      ?.provider  
      ?.registerWandInKotlin("Test") { params,_ -> params.player.sendMessage("Test successful!") }  
  }  
}
```
Java
```java
import com.hoshikurama.github.mcwandsframework.MCWandsService;  
import org.bukkit.Bukkit;  
import org.bukkit.plugin.RegisteredServiceProvider;  
import org.bukkit.plugin.java.JavaPlugin;  
  
import java.util.Optional;  
  
public class TestWand extends JavaPlugin {

  @Override  
  public void onEnable() {

    Optional.ofNullable(Bukkit.getServicesManager().getRegistration(MCWandsService.class))
        .map(RegisteredServiceProvider::getProvider)  
        .ifPresent( service -> {  
          service.registerWandInJava("Test", p -> p.getPlayer().sendMessage("Test successful!"));  
        });
  }  
}
```
# Function Parameter Information
Java users and Kotlin users alike have access to a wand parameter object when defining the wand behaviour function. This object contains the following properties:
| Property | Description |
| --- | --- |
| player | Player using wand |
| cooldown | Specialty cooldown value on wand |
| intensity | Multi-purpose value representing the strength of the specialty action |
| range | Multi-purpose value representing the range of the specialty action |
| simpleScheduler | More explained below |
### SimpleScheduler
SimpleScheduler is a light wrapper for the Bukkit Scheduler that abstracts away the main plugin from the end developer. This class does not contain any function for accepting a BukkitRunable object, so if this is something you need, you may retrieve the underlying plugin object from this object.
# Kotlin
MCWandsFramework was written entirely in Kotlin. Furthermore, Kotlin users have access to more parts of this framework than Java users, most namely coroutines. If you don't know what coroutines are, no worries! The coroutine part of this framework can be completely ignored! Just treat your code like any other code you would write! For those who do have experience with coroutines, all wand actions by default run on a coroutine in the main thread. When registering a wand with Kotlin, users have access to both the specialty parameters of the wand and the CoroutineScope object inside the registered function.

In order to use the coroutines, Kotlin users must add these extra dependencies:

Kotlin DSL (build.gradle.kts)
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0")
implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0")
```
Groovy (build.gradle)
```groovy
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0'
implementation 'com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:1.5.0'
implementation 'com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:1.5.0'
```
Maven (pom.xml)
```xml
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx-coroutines-core</artifactId>
    <version>1.5.0</version>
  </dependency>
<dependency>
    <groupId>com.github.shynixn.mccoroutine</groupId>
    <artifactId>mccoroutine-bukkit-api</artifactId>
    <version>1.5.0</version>
  </dependency>
<dependency>
    <groupId>com.github.shynixn.mccoroutine</groupId>
    <artifactId>mccoroutine-bukkit-core</artifactId>
    <version>1.5.0</version>
  </dependency>
```
