package com.hoshikurama.github.mcwandsframework

import kotlinx.coroutines.coroutineScope
import org.bukkit.entity.Player
import java.time.Instant
import java.util.*

internal class Registry {
    private val registry: MutableMap<String, KotlinWandFunction> = mutableMapOf()

    internal fun keys() = registry.keys.toList()

    internal fun putIfAbsent(name: String, action: KotlinWandFunction): Any? =
        registry.putIfAbsent(name, action)

    internal suspend fun runWand(type: String, parameters: WandData, container: ContextContainer) =
        coroutineScope { registry[type]?.execute(parameters, container) }
}

class Cooldowns {
    private val map: MutableMap<String, MutableMap<UUID, Instant>> = mutableMapOf()

    internal fun getInstantOrNull(type: String, uuid: UUID) =
        map[type]?.get(uuid)

    internal fun underCooldown(player: Player, type: String) =
        map[type]?.get(player.uniqueId)?.isAfter(Instant.now()) ?: false

    internal fun addToCooldown(player: Player, type: String, seconds: Double) {
        map[type]?.put(player.uniqueId, Instant.now().plusMillis((seconds * 1000L).toLong()))
    }

    internal fun registerType(name: String) {
        map[name] = mutableMapOf()
    }

    internal fun optimize() {
        val now = Instant.now()
        map.forEach { m ->
            m.value.forEach {
                if (it.value.isAfter(now))
                    m.value.remove(it.key)
            }
        }
    }
}

internal inline fun <T> tryOrNull(function: () -> T): T? =
    try { function() }
    catch (ignored: Exception) { null }