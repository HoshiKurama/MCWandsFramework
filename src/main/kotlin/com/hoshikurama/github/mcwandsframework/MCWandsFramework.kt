package com.hoshikurama.github.mcwandsframework

import com.github.shynixn.mccoroutine.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.registerSuspendingEvents
import com.github.shynixn.mccoroutine.setSuspendingExecutor
import com.github.shynixn.mccoroutine.setSuspendingTabCompleter
import com.hoshikurama.github.mcwandsframework.events.Commands
import com.hoshikurama.github.mcwandsframework.events.PlayerInteractionEvent
import com.hoshikurama.github.mcwandsframework.events.TabCompletion

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.ServicePriority

internal val mainPlugin: MCWandsFramework
    get() = MCWandsFramework.instance


class MCWandsFramework : SuspendingJavaPlugin() {
    internal val cooldowns = Cooldowns()
    internal val registry = Registry()
    private val service = MCWandsServiceClass()

    private lateinit var metrics: Metrics

    companion object {
        internal lateinit var instance: MCWandsFramework
    }

    override fun onEnable() {
        instance = this
        metrics = Metrics(this, 11711)
        cooldowns.registerType("Normal")

        server.pluginManager.registerSuspendingEvents(PlayerInteractionEvent(), this)
        getCommand("mcwands")?.setSuspendingTabCompleter(TabCompletion())
        getCommand("mcwands")?.setSuspendingExecutor(Commands())

        Bukkit.getServicesManager().register(MCWandsService::class.java, service, this, ServicePriority.Normal)

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, cooldowns::optimize,0,6000L)
    }
}

internal fun String.addColour() = ChatColor.translateAlternateColorCodes('&', this)

