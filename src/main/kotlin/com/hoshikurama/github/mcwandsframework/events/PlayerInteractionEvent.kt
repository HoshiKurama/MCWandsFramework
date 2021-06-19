package com.hoshikurama.github.mcwandsframework.events

import com.github.shynixn.mccoroutine.scope
import com.hoshikurama.github.mcwandsframework.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.time.Instant

class PlayerInteractionEvent : Listener {

    @EventHandler
    suspend fun onPlayerInteract(event: PlayerInteractEvent) {
        when (event.action) {
            Action.RIGHT_CLICK_AIR,
            Action.RIGHT_CLICK_BLOCK,
            Action.LEFT_CLICK_AIR -> {
                val player = event.player
                val item = player.inventory.itemInMainHand

                val wand: WandData? =
                    if (event.action == Action.LEFT_CLICK_AIR)
                        getNormalWandOrNull(player, item)
                    else getSpecialtyWandOrNull(player, item)

                if (wand != null) {
                    val cooldowns = mainPlugin.cooldowns

                    if (cooldowns.underCooldown(player, wand.type)) {
                        player.sendMessage("&3Your wand still has a cooldown of ${cooldowns.getInstantOrNull("Normal", player.uniqueId)!!.epochSecond - Instant.now().epochSecond} seconds!".addColour())
                        return
                    }
                    if (!player.hasPermission("mcwands.use")) {
                        player.sendMessage("&cYou do not have permission to use wands!".addColour())
                        return
                    }

                    mainPlugin.cooldowns.addToCooldown(player, wand.type, wand.cooldown)
                    try { mainPlugin.registry.runWand(wand.type, wand, mainPlugin.scope) }
                    catch (e: Exception) { event.player.sendMessage("&cAn error occurred while using this wand!".addColour()) }
                }
            }
            else -> return
        }
    }
}