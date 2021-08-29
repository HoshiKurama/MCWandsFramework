package com.github.hoshikurama.mcwandsframework.events

import com.github.hoshikurama.mcwandsframework.mainPlugin
import com.github.shynixn.mccoroutine.SuspendingTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TabCompletion : SuspendingTabCompleter {

    /*
       /mcwands createNormal <Damage (half-hearts)> <Cooldown (sec)> <Range (m)> [Name]
       /mcwands createSpecial <Type> <Normal Damage (half-hearts)> <Normal Cooldown (sec)> <Normal Range (m)> <Special Intensity> <Special Cooldown (sec)> <Special Range> [Name]
    */
    override suspend fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        fun List<String>.filterStartsWith() = filter { it.startsWith(args[args.lastIndex]) }
        fun blankList() = listOf("")

        if (sender is Player && !sender.hasPermission("mcwands.tab-complete")) return blankList()

        return if (args.size <= 1) listOf("createNormal", "createSpecial").filterStartsWith()
        else when (args[0]) {
            "createNormal" -> when (args.size) {
                2 -> listOf("<Damage (half-hearts)>").filterStartsWith()
                3 -> listOf("<Cooldown (sec)>").filterStartsWith()
                4 -> listOf("<Range (m)>").filterStartsWith()
                5 -> listOf("[Name]").filterStartsWith()
                else -> blankList()
            }
            "createSpecial" -> when (args.size) {
                2 -> (listOf("<Type>") + mainPlugin.registry.keys()).filterStartsWith()
                3 -> listOf("<Normal Damage (half-hearts)>").filterStartsWith()
                4 -> listOf("<Normal Cooldown (sec)>").filterStartsWith()
                5 -> listOf("<Normal Range (m)>").filterStartsWith()
                6 -> listOf("<Special Intensity>").filterStartsWith()
                7 -> listOf("<Special Cooldown (sec)>").filterStartsWith()
                8 -> listOf("<Special Range>").filterStartsWith()
                9 -> listOf("[Name]").filterStartsWith()
                else -> blankList()
            }
            else -> blankList()
        }
    }
}