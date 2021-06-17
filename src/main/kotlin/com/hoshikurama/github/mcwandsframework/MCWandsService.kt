package com.hoshikurama.github.mcwandsframework

import org.bukkit.Bukkit
import java.util.logging.Level

class MCWandsService {

    /**
     * Register wand from Kotlin code. JAVA USERS CANNOT USE THIS!
     * @param name Name of wand to register
     * @param action suspended function of type (KotlinWand, CoroutineScope) -> Unit.
     * CoroutineScope contains the current scope of the coroutine.
     * KotlinWand contains the parameters an end user might want. Check out the class for more information.
     */
    @Suppress("unused")
    fun registerWandInKotlin(name: String, action: KotlinParameterFunction) {
        val wasAdded = mainPlugin.registry.putIfAbsent(name, action)
        logRegisterResult(wasAdded, name)
    }

    /**
     * Register wand from Java code. Kotlin users may use this as well, but it is strongly advised they use registerWandInKotlin instead.
     * @param name Name of wand to register
     * @param action Consumer<JavaWand> function.
     * JavaWand contains the parameters an end user might want to use. Check out the class for more information.
     */
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