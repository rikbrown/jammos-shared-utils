package net.jammos.utils.extensions

import kotlinx.coroutines.experimental.future.await
import java.util.concurrent.CompletionStage

/**
 * Await every completion stage
 */
suspend fun Iterable<CompletionStage<*>>.awaitAll() = forEach { it.await() }