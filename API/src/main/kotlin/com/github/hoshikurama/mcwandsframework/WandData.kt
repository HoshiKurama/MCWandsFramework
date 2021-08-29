package com.github.hoshikurama.mcwandsframework

import org.bukkit.entity.Player

interface WandData {
    val player: Player
    val cooldown: Double
    val intensity: Int
    val range: Int
}