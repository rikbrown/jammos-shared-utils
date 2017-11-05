package net.jammos.utils.extensions

import kotlinx.coroutines.experimental.future.await
import java.util.concurrent.CompletionStage

suspend fun Iterable<CompletionStage<*>>.awaitAll() = forEach { it.await() }