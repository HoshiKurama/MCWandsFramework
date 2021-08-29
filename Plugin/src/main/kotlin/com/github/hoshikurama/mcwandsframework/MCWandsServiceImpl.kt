package com.github.hoshikurama.mcwandsframework

import org.bukkit.Bukkit
import java.util.function.Consumer
import java.util.logging.Level

class MCWandsServiceImpl : MCWandsService {

    override fun registerWandKotlin(name: String, action: KotlinWandFunction): Boolean {
        return mainPlugin.registry.putIfAbsent(name, action)
            .run { this == null }
            .apply {
                if (this) {
                    logIfSuccessful(name)
                    mainPlugin.cooldowns.registerType(name)
                }
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
        Bukkit.getLogger().log(
            Level.INFO,
            "[MCWandsFramework] Registered $name successfully!")
    }
}