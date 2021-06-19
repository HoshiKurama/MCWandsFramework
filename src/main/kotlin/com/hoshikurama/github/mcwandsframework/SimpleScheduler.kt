package com.hoshikurama.github.mcwandsframework

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * Simple scheduling class that abstracts away the plugin instance in the function.
 * Plugin is still available as a type Plugin.
 */
class SimpleScheduler(
    val plugin: Plugin,
    val scheduler: BukkitScheduler,
) {
    @Suppress("unused")
    fun scheduleSyncDelayedTask(delay: Long, task: Runnable): Int =
        scheduler.scheduleSyncDelayedTask(plugin, task, delay)

    @Suppress("unused")
    fun scheduleSyncRepeatingTask(period: Long, delay: Long = 0, task: Runnable): Int =
        scheduler.scheduleSyncRepeatingTask(plugin, task, delay, period)

    @Suppress("unused")
    fun <T> callSyncMethod(task: Callable<T>): Future<T> =
        scheduler.callSyncMethod(plugin, task)

    @Suppress("unused")
    fun runTask(task: Runnable): Int =
        scheduler.runTask(plugin, task).taskId

    @Suppress("unused")
    fun runTaskAsynchronously(task: Runnable): Int =
        scheduler.runTaskAsynchronously(plugin, task).taskId

    @Suppress("unused")
    fun runTaskLaterAsynchronously(delay: Long, task: Runnable): Int =
        scheduler.runTaskLaterAsynchronously(plugin, task, delay).taskId

    @Suppress("unused")
    fun runTaskTimer(period: Long, delay: Long = 0, task: Runnable): Int =
        scheduler.runTaskTimer(plugin, task, delay, period).taskId

    @Suppress("unused")
    fun runTaskTimerAsynchronously(period: Long, delay: Long = 0, task: Runnable): Int =
        scheduler.runTaskTimerAsynchronously(plugin, task, delay, period).taskId
}