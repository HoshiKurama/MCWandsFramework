package com.github.hoshikurama.mcwandsframework

import com.github.hoshikurama.mcwandsframework.events.Commands
import com.github.hoshikurama.mcwandsframework.events.PlayerInteractionEvent
import com.github.hoshikurama.mcwandsframework.events.TabCompletion
import com.github.hoshikurama.mcwandsframework.service.MCWandsService
import com.github.hoshikurama.mcwandsframework.service.MCWandsServiceImpl
import com.github.shynixn.mccoroutine.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.registerSuspendingEvents
import com.github.shynixn.mccoroutine.setSuspendingExecutor
import com.github.shynixn.mccoroutine.setSuspendingTabCompleter
import kotlinx.coroutines.delay
import org.bukkit.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.ServicePriority

internal val mainPlugin: MCWandsFramework
    get() = MCWandsFramework.instance


class MCWandsFramework : SuspendingJavaPlugin() {
    internal val cooldowns = Cooldowns()
    internal val registry = Registry()
    private val service = MCWandsServiceImpl()

    private lateinit var metrics: Metrics

    companion object {
        internal lateinit var instance: MCWandsFramework
    }

    override fun onEnable() {
        instance = this
        metrics = Metrics(this, 11711)

        registerPrimaryFire()

        server.pluginManager.registerSuspendingEvents(PlayerInteractionEvent(), this)
        getCommand("mcwands")?.setSuspendingTabCompleter(TabCompletion())
        getCommand("mcwands")?.setSuspendingExecutor(Commands())

        Bukkit.getServicesManager().register(MCWandsService::class.java, service, this, ServicePriority.Normal)

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, cooldowns::optimize,0,6000L)
    }

    private fun registerPrimaryFire() {
        service.registerWandKotlin("Normal") { params, _ ->
            fun Any.notEquals(any: Any?) = this != any
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
}

internal fun String.addColour() = ChatColor.translateAlternateColorCodes('&', this)

