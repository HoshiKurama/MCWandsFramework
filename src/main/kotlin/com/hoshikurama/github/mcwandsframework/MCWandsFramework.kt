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
    internal val registry = Registry()
    internal val cooldowns = Cooldowns()

    private lateinit var metrics: Metrics

    companion object {
        internal lateinit var instance: MCWandsFramework
    }

    override fun onEnable() {
        instance = this
        metrics = Metrics(this, 11711)

        server.pluginManager.registerSuspendingEvents(PlayerInteractionEvent(), this)
        getCommand("mcwands")?.setSuspendingTabCompleter(TabCompletion())
        getCommand("mcwands")?.setSuspendingExecutor(Commands())

        Bukkit.getServicesManager().register(MCWandsService::class.java, MCWandsService(), this, ServicePriority.Normal)
    }
}

internal fun String.addColour() = ChatColor.translateAlternateColorCodes('&', this)
