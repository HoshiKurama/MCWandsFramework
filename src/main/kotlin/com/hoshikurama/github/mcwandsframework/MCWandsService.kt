package com.hoshikurama.github.mcwandsframework

import org.bukkit.Bukkit
import java.util.logging.Level

class MCWandsService {

    @Suppress("unused")
    fun registerWandInKotlin(name: String, action: KotlinParameterFunction) {
        val wasAdded = mainPlugin.registry.putIfAbsent(name, action)
        logRegisterResult(wasAdded, name)
    }

    @Suppress("unused")
    fun registerWandInJava(name: String, action: JavaParameterFunction) {
        // Wraps Java function with Kotlin wrapper
        val wrapperFunction: KotlinParameterFunction = { params,_ ->
            val javaWand = JavaWand(
                params.player,
                params.cooldown,
                params.intensity,
                params.range,
                params.type
            )
            action.accept(javaWand)
        }

        val wasAdded = mainPlugin.registry.putIfAbsent(name, wrapperFunction)
        logRegisterResult(wasAdded, name)
    }

    private fun logRegisterResult(result: Any?, name: String) {
        Bukkit.getLogger().run {
            if (result  == null) log(Level.INFO,"[MCWandsFramework] Registered $name successfully!")
            else log(Level.WARNING, "[MCWandsFramework] Name conflict for $name! Only first registration will be used! Please contact the developer of the specialty wand and suggest they change the wand name.")
        }
    }
}