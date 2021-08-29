package com.github.hoshikurama.mcwandsframework.service

import com.github.hoshikurama.mcwandsframework.ContextContainer
import com.github.hoshikurama.mcwandsframework.WandData
import java.util.function.Consumer

interface MCWandsService {
    /**
     * Registers a wand with MCWandsFramework.
     *
     * Safe for Kotlin users ONLY!
     * @param name String for wand name
     * @param action KotlinWandFunction describing wand code
     * @return Boolean describing if wand registration was successful. Returns false if registry already contains a wand with the specified name.
     * @see KotlinWandFunction
     */
    fun registerWandKotlin(name: String, action: KotlinWandFunction): Boolean

    /**
     * Registers a wand with MCWandsFramework.
     *
     * Safe for Java users and Kotlin users.
     * @param name String for wand name
     * @param action Consumer<WandData> describing wand code
     * @return Boolean describing if wand registration was successful. Returns false if registry already contains a wand with the specified name.
     */
    fun registerWandJava(name: String, action: Consumer<WandData>): Boolean
}

/**
 * SAM interface for Kotlin wand registration. Provides parameters for WandData and CoroutineScope
 */
fun interface KotlinWandFunction {
    suspend fun execute(params: WandData, contexts: ContextContainer)
}