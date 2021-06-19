package com.hoshikurama.github.mcwandsframework

import kotlinx.coroutines.CoroutineScope
import org.bukkit.Bukkit
import java.util.function.Consumer
import java.util.logging.Level

interface MCWandsService {
    fun registerWandKotlin(name: String, action: KotlinWandFunction): Boolean
    fun registerWandJava(name: String, action: Consumer<WandData>): Boolean
}

fun interface KotlinWandFunction {
    suspend fun execute(params: WandData, scope: CoroutineScope)
}


class MCWandsServiceClass : MCWandsService {

    override fun registerWandKotlin(name: String, action: KotlinWandFunction): Boolean =
        mainPlugin.registry.putIfAbsent(name, action)
            .run { (equals(null)) }
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