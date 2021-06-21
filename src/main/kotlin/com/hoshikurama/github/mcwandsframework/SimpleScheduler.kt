package com.hoshikurama.github.mcwandsframework

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * Light wrapper for BukkitScheduler functions. Scheduler executes with MCWandsFramework as task owner.
 * @property plugin MCWandsFramework plugin instance
 * @property scheduler BukkitScheduler for server
 */
class SimpleScheduler(
    val plugin: Plugin,
    val scheduler: BukkitScheduler,
) {
    /**
     * @return task ID
     * @see BukkitScheduler.scheduleSyncDelayedTask
     */
    @Suppress("unused")
    fun scheduleSyncDelayedTask(delay: Long, task: Runnable): Int =
        scheduler.scheduleSyncDelayedTask(plugin, task, delay)

    /**
     * @return task ID
     * @see BukkitScheduler.scheduleSyncRepeatingTask
     */
    @Suppress("unused")
    fun scheduleSyncRepeatingTask(period: Long, delay: Long = 0, task: Runnable): Int =
        scheduler.scheduleSyncRepeatingTask(plugin, task, delay, period)

    /**
     * @return Future object related to the task
     * @see BukkitScheduler.callSyncMethod
     */
    @Suppress("unused")
    fun <T> callSyncMethod(task: Callable<T>): Future<T> =
        scheduler.callSyncMethod(plugin, task)

    /**
     * @return task ID
     * @see BukkitScheduler.runTask
     */
    @Suppress("unused")
    fun runTask(task: Runnable): Int =
        scheduler.runTask(plugin, task).taskId

    /**
     * @return task ID
     * @see BukkitScheduler.runTaskAsynchronously
     */
    @Suppress("unused")
    fun runTaskAsynchronously(task: Runnable): Int =
        scheduler.runTaskAsynchronously(plugin, task).taskId

    /**
     * @return task ID
     * @see BukkitScheduler.runTaskLaterAsynchronously
     */
    @Suppress("unused")
    fun runTaskLaterAsynchronously(delay: Long, task: Runnable): Int =
        scheduler.runTaskLaterAsynchronously(plugin, task, delay).taskId

    /**
     * @return task ID
     * @see BukkitScheduler.runTaskTimer
     */
    @Suppress("unused")
    fun runTaskTimer(period: Long, delay: Long = 0, task: Runnable): Int =
        scheduler.runTaskTimer(plugin, task, delay, period).taskId

    /**
     * @return task ID
     * @see BukkitScheduler.runTaskTimerAsynchronously
     */
    @Suppress("unused")
    fun runTaskTimerAsynchronously(period: Long, delay: Long = 0, task: Runnable): Int =
        scheduler.runTaskTimerAsynchronously(plugin, task, delay, period).taskId
}