package com.github.hoshikurama.mcwandsframework.events

import com.github.hoshikurama.mcwandsframework.addColour
import com.github.hoshikurama.mcwandsframework.tryOrNull
import com.github.shynixn.mccoroutine.SuspendingCommandExecutor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class Commands : SuspendingCommandExecutor {

    override suspend fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (sender is Player && !sender.hasPermission("mcwands.create")) {
            sender.sendMessage("&cYou do not have permission to perform this command!".addColour())
        }

        fun getWandOrNull(
            nIntensityStr: String,
            nCooldownStr: String,
            nRangeStr: String,
            creationTypeStr: String,
            name: String,
            sIntensityStr: String? = null,
            sCooldownStr: String? = null,
            sRangeStr: String? = null,
            sType: String = "NULL",
        ) = tryOrNull {
            val isSpecial = creationTypeStr == "Special"
            val wand = ItemStack(Material.STICK, 1)

            infix fun String.appendIfSpecial(str: String) = if (isSpecial) "$this$str" else this

            val loreText = listOf(
                "&7&lWand Type:&r ${if (isSpecial) "&6$sType" else "&3Normal"}",
                "&8&lCooldown:&r &3$nCooldownStr" appendIfSpecial "&8&l/&r&6$sCooldownStr",
                "&8&lRange:&r &3$nRangeStr" appendIfSpecial "&8&l/&r&6$sRangeStr",
                "&8&lIntensity:&r &3$nIntensityStr" appendIfSpecial "&8&l/&r&6$sIntensityStr",
                " ",
                "&9MCWands"
            )
                .map(String::addColour)

            val meta = wand.itemMeta
            meta.setDisplayName(name.addColour())
            meta.lore = loreText
            wand.itemMeta = meta
            wand //returns
        }

        val wand = tryOrNull {
            when (args[0]) {
                "createNormal" -> getWandOrNull(
                    nIntensityStr = args[1],
                    nCooldownStr = args[2],
                    nRangeStr = args[3],
                    name = if (args.size >= 5) args[4] else "&3&lWand",
                    creationTypeStr = "Normal"
                )
                "createSpecial" -> getWandOrNull(
                    nIntensityStr = args[2],
                    nCooldownStr = args[3],
                    nRangeStr = args[4],
                    sIntensityStr = args[5],
                    sCooldownStr = args[6],
                    sRangeStr = args[7],
                    name = if (args.size >= 9) args[8] else "&3&lWand",
                    sType = args[1],
                    creationTypeStr = "Special"
                )
                else -> null
            }
        }

        if (wand != null)
            (sender as? Player)?.inventory?.addItem(wand)?.also { sender.sendMessage("&6[MinecraftWands]:&6 Wand sent successfully!".addColour()) }
        else sender.sendMessage("&6[MinecraftWands]:&6 Please refer to the tab-complete for proper usage of this command.")

        return true

    }
}