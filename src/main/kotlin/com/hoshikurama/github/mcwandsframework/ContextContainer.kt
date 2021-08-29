package com.hoshikurama.github.mcwandsframework

import kotlin.coroutines.CoroutineContext

class ContextContainer(
    val main: CoroutineContext,
    val async: CoroutineContext,
)