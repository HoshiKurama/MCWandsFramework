package com.hoshikurama.github.mcwandsframework

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.time.Instant
import java.util.*

internal class Registry {
    private val registry: MutableMap<String, KotlinWandFunction> = mutableMapOf()

    init {
        registry["Normal"] = KotlinWandFunction { params, _ ->
            val particleLocation = params.player.eyeLocation.clone()
            val particleDirectionAdd = particleLocation.direction.clone().multiply(0.25)

            //Makes noise for all nearby players
            Bukkit.getOnlinePlayers()
                .filter { it.location.distance(particleLocation) <= 15.0 }
                .forEach { it.playSound(it.eyeLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 100f, 3.5f) }

            // Animated path and raytrace. Frequently ray-traces to simulate velocity
            for (i in 2..params.range step 2) {
                // Trace
                val traceResult = params.player.world.rayTrace(
                    particleLocation,
                    particleDirectionAdd,
                    2.0,
                    FluidCollisionMode.NEVER,
                    true,
                    1.02,
                    null
                )

                //Particles
                repeat(8) {
                    params.player.world.spawnParticle(Particle.SPELL_INSTANT, particleLocation, 1)
                    particleLocation.add(particleDirectionAdd)
                }

                if (traceResult != null) {
                    if (traceResult.hitBlock != null) break
                    val result = sequenceOf(traceResult.hitEntity)
                        .filterNotNull()
                        .filterIsInstance<LivingEntity>()
                        .filter {
                            (it as? Player)?.uniqueId?.notEquals(params.player.uniqueId) ?: true
                        } // Filters out self
                        .map { it.apply { damage(params.intensity.toDouble(), params.player) } }
                        .firstOrNull()

                    if (result != null) break
                } else delay(25L)
            }
        }
    }

    internal fun keys() = registry.keys.toList()

    internal fun putIfAbsent(name: String, action: KotlinWandFunction): Any? =
        registry.putIfAbsent(name, action)

    internal suspend fun runWand(type: String, parameters: WandData, coroutineScope: CoroutineScope) =
        registry[type]?.execute(parameters, coroutineScope)
}

private fun Any.notEquals(any: Any?) = this != any


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