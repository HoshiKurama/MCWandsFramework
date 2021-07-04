package com.hoshikurama.github.mcwandsframework

import kotlinx.coroutines.CoroutineScope
import org.bukkit.Bukkit
import java.util.function.Consumer
import java.util.logging.Level

interface MCWandsService {
    /**
     * Registers a wand with MCWandsFramework.
     *
     * Safe for Kotlin users ONLY!
     * @param name String for wand name
     * @param action KotlinWandFunction describing wand code
     * @return Boolean describing if wand registration was successful. Returns false if registry already contains a wand with the specified name.
     * @see KotlinWandFunction
     */
    fun registerWandKotlin(name: String, action: KotlinWandFunction): Boolean

    /**
     * Registers a wand with MCWandsFramework.
     *
     * Safe for Java users and Kotlin users.
     * @param name String for wand name
     * @param action Consumer<WandData> describing wand code
     * @return Boolean describing if wand registration was successful. Returns false if registry already contains a wand with the specified name.
     */
    fun registerWandJava(name: String, action: Consumer<WandData>): Boolean
}

/**
 * SAM interface for Kotlin wand registration. Provides parameters for WandData and CoroutineScope
 */
fun interface KotlinWandFunction {
    suspend fun execute(params: WandData, scope: CoroutineScope)
}


class MCWandsServiceClass : MCWandsService {

    override fun registerWandKotlin(name: String, action: KotlinWandFunction): Boolean =
        mainPlugin.registry.putIfAbsent(name, action)
            .run { this == null }
            .apply {
                if (this) {
                    logIfSuccessful(name)
                    mainPlugin.cooldowns.registerType(name)
                }
            }

    override fun registerWandJava(name: String, action: Consumer<WandData>): Boolean {
        // Wraps Java function with Kotlin wrapper
        val wrapperFunction = KotlinWandFunction { params, _ -> action.accept(params) }

        return mainPlugin.registry.putIfAbsent(name, wrapperFunction)
            .run { (equals(null)) }
            .apply {
                if (this) {
                    logIfSuccessful(name)
                    mainPlugin.cooldowns.registerType(name)
                }
            }
    }

    private fun logIfSuccessful(name: String) {
        Bukkit.getLogger().log(Level.INFO,
            "[MCWandsFramework] Registered $name successfully!")
    }
}