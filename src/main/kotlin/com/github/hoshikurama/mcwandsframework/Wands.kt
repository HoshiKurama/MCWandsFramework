package com.github.hoshikurama.mcwandsframework

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * During normal shots, object stores primary fire data.
 * During specialty wand shots, object stores specialty wand data if applicable.
 * @property player Player using wand
 * @property cooldown Cooldown in seconds for which user will be added to the wand's associated cooldown
 * @property intensity Multi-purpose used to describe the general strength of the wand
 * @property range general range of the wand
 */
class WandData(
    val player: Player,
    val cooldown: Double,
    val intensity: Int,
    val range: Int,
    internal val type: String,
)

internal fun getSpecialtyWandOrNull(player: Player, item: ItemStack): WandData? {
    return if (item.notMCWand()) null
    else getWandOrNull(player, item, 1, isNormal = false)
}

internal fun getNormalWandOrNull(player: Player, item: ItemStack): WandData? {
    return if (item.notMCWand()) null
    else getWandOrNull(player, item, 0, isNormal = true)
}

private fun ItemStack.isMCWand() = type == Material.STICK && itemMeta?.lore?.run { ChatColor.stripColor(get(5)) == "MCWands" } ?: false
private fun ItemStack.notMCWand() = !isMCWand()

private fun getWandOrNull(player: Player, item: ItemStack, statColumn: Int, isNormal: Boolean = false): WandData? {
    return tryOrNull {
        val lore = item.itemMeta.lore!!

        WandData(
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