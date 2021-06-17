package com.hoshikurama.github.mcwandsframework

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * Simple scheduling class for Kotlin users that abstracts away the scheduler and main plugin instance
 * for BukkitScheduler needs that involve the plugin class. Uses functions instead of Runnables.
 * Java users should NEVER use this
 **/
class SimpleSchedulerKotlin(
    private val plugin: Plugin,
    private val scheduler: BukkitScheduler,
) {
    @Suppress("unused")
    fun scheduleSyncDelayedTask(delay: Long, task: () -> Unit): Int =
        scheduler.scheduleSyncDelayedTask(plugin, Runnable(task), delay)

    @Suppress("unused")
    fun scheduleSyncRepeatingTask(period: Long, delay: Long = 0, task: () -> Unit): Int =
        scheduler.scheduleSyncRepeatingTask(plugin, Runnable(task), delay, period)

    @Suppress("unused")
    fun <T> callSyncMethod(task: () -> T): Future<T> =
        scheduler.callSyncMethod(plugin, Callable(task))

    @Suppress("unused")
    fun runTask(task: () -> Unit): Int =
        scheduler.runTask(plugin, Runnable(task)).taskId

    @Suppress("unused")
    fun runTaskAsynchronously(task: () -> Unit): Int =
        scheduler.runTaskAsynchronously(plugin, Runnable(task)).taskId

    @Suppress("unused")
    fun runTaskLaterAsynchronously(delay: Long, task: () -> Unit): Int =
        scheduler.runTaskLaterAsynchronously(plugin, Runnable(task), delay).taskId

    @Suppress("unused")
    fun runTaskTimer(period: Long, delay: Long = 0, task: () -> Unit): Int =
        scheduler.runTaskTimer(plugin, Runnable(task), delay, period).taskId

    @Suppress("unused")
    fun runTaskTimerAsynchronously(period: Long, delay: Long = 0, task: () -> Unit): Int =
        scheduler.runTaskTimerAsynchronously(plugin, Runnable(task), delay, period).taskId
}

/**
 * Simple scheduling class for Java users that abstracts away the scheduler and main plugin instance
 * for BukkitScheduler needs that involve the plugin class.
 * Uses Runnables instead of functions.
 */
class SimpleSchedulerJava(
    private val plugin: Plugin,
    private val scheduler: BukkitScheduler,
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