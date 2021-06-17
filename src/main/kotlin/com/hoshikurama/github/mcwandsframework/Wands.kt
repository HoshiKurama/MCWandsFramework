package com.hoshikurama.github.mcwandsframework

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

sealed class SharedWandData(
    val player: Player,
    val cooldown: Double,
    val intensity: Int,
    val range: Int,
    val type: String
)

/*
NOTE:
    JavaWand class should not be used for anything other than end-users creating wands with Java. DO NOT
    USE INTERNALLY. When Java-based functions are registered into the system, they are converted into
    a Kotlin function that wraps around the Java function
 */
class JavaWand(
    player: Player,
    cooldown: Double,
    intensity: Int,
    range: Int,
    type: String
) : SharedWandData(player, cooldown, intensity, range, type) {
    val simpleScheduler = SimpleSchedulerJava(mainPlugin, Bukkit.getScheduler())
}

class KotlinWand(
    player: Player,
    cooldown: Double,
    intensity: Int,
    range: Int,
    type: String,
) : SharedWandData(player, cooldown, intensity, range, type) {
    val simpleScheduler = SimpleSchedulerKotlin(mainPlugin, Bukkit.getScheduler())
}

internal fun getSpecialtyWandOrNull(player: Player, item: ItemStack): KotlinWand? {
    return if (item.notMCWand()) null
    else getWandOrNull(player, item, 1)
}

internal fun getNormalWandOrNull(player: Player, item: ItemStack): KotlinWand? {
    return if (item.notMCWand()) null
    else getWandOrNull(player, item, 0, isNormal = true)
}

private fun getWandOrNull(player: Player, item: ItemStack, statColumn: Int, isNormal: Boolean = false): KotlinWand? {
    return tryOrNull {
        val lore = item.itemMeta.lore!!

        KotlinWand(
            player = player,
            cooldown = ChatColor.stripColor(lore[1])!!
                .split(" ")[1]
                .split("/")[statColumn]
                .toDouble(),
            range = ChatColor.stripColor(lore[2])!!
                .split(" ")[1]
                .split("/")[statColumn]
                .toInt(),
            intensity = ChatColor.stripColor(lore[3])!!
                .split(" ")[1]
                .split("/")[statColumn]
                .toInt(),
            type =
            if (isNormal) "Normal"
            else ChatColor.stripColor(lore[0])!!
                .split(": ")[1]
        )
    }
}

private fun ItemStack.isMCWand() = type == Material.STICK && itemMeta?.lore?.run { ChatColor.stripColor(get(5)) == "MCWands" } ?: false
private fun ItemStack.notMCWand() = !isMCWand()

private inline fun <T> tryOrNull(action: () -> T): T? {
    return try { action() }
    catch (ignored: Exception) { null }
}