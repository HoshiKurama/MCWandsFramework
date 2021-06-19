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
mcWandsService?.registerWandKotlin("WandName") { params, scope ->  
  /* Your code here! */
}

// ...or it may be first assigned to a variable!
val wandCode = KotlinWandFunction { params, scope ->    
  /* You code here */  
}  
mcWandsService?.registerWandKotlin("WandNameHere", wandCode)
```
Java
```java
// Wands can be registered the classic way:  
RegisteredServiceProvider<MCWandsService> rsp = Bukkit.getServicesManager().getRegistration(MCWandsService.class);  
  
if (rsp != null) {  
  MCWandsService mcWandsService = rsp.getProvider();  
  // Wand code may be defined at registration...  
  mcWandsService.registerWandJava("WandNameHere", params -> {
    /* Your code here */
  });  
  
  // ...or it may be first assigned to a variable!  
  Consumer<WandData> wandCode = params -> {
    /* Your code here */
  };  
  mcWandsService.registerWandJava("WandNameHere", wandCode);  
}  
  
  
// Java optionals can be used instead!:  
Optional.ofNullable(Bukkit.getServicesManager().getRegistration(MCWandsService.class))
    .map(RegisteredServiceProvider::getProvider)  
    .ifPresent( service -> {  
      service.registerWandJava("WandNameHere", params -> {  
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
      ?.run {
        registerWandKotlin("Fire", FireWand)
        registerWandKotlin("Name") { params,_ -> 
          params.player.sendMessage("Your name is: ${params.player.name}!") 
        }
      }
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
          service.registerWandJava("Fire", fireWand);
          service.registerWandJava("Name", params -> {
            Player player = params.getPlayer();
            player.sendMessage("Your name is: " + player.getName() + "!");
          });
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
SimpleScheduler is a light wrapper for the Bukkit Scheduler that abstracts away the main plugin from the end developer. This class only contains functions that accept code as a Runnable. Any users desiring to use the BukkitScheduler class can still use this by retrieving the Plugin instance contained by this object.
# Kotlin
MCwandsFramework was written in Kotlin. Kotlin users will have a more seamless experience when developing extra wands. However, Kotlin users also have access to coroutines. MCWandsFramework utilizes the MCCoroutines library.  

If you're unaware of what coroutines are or don't want to use them, don't worry! They're not required to use, so feel free to write your code as usual. For those who do desire to use coroutines, the SAM (functional) interface KotlinWandFunction used to register Kotlin wands has a parameter for the wand data and a parameter for the coroutine scope. This interface is furthermore a suspend function. All wands begin execution on a coroutine working in the main thread.

MCCoroutines has two dispatchers: one for the main thread and another for non-main threads. Calling async {} without specifying the async coroutine context results in execution still on the main thread. To utilize coroutines on other threads, the async dispatcher coroutine context must be acquired. This can be done in the following way:
```kotlin
// This gets the async dispatcher coroutine context
val asyncContext = (params.simpleScheduler.plugin as SuspendingJavaPlugin).asyncDispatcher

// scope is already the main-thread coroutine context, but it can still be retrieved in the following way
val mainContext = (params.simpleScheduler.plugin as SuspendingJavaPlugin).minecraftDispatcher
```

**IMPORTANT INFORMATION:** Kotlin users, please do **NOT** shade the following dependencies into your custom wand Jar:
* MCCoroutines
* Kotlin coroutine library
* Kotlin standard library

These are provided during runtime by the framework. Shading any of these dependencies in or allowing Spigot/Paper to download these libraries for your custom wands will cause a ClassLoader exception. Any dependencies aside from these are okay to shade into your Jar.
