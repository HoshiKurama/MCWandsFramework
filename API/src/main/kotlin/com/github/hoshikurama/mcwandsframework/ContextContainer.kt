package com.github.hoshikurama.mcwandsframework

import kotlin.coroutines.CoroutineContext

class ContextContainer(
    val main: CoroutineContext,
    val async: CoroutineContext,
)