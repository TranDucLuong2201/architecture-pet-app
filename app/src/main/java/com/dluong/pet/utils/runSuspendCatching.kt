package com.dluong.pet.utils

import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * A utility function to run a suspend block of code and catch any exceptions that occur.
 *
 * This function is useful for handling exceptions in coroutines without having to use try-catch blocks
 * directly in the coroutine code.
 *
 * @param context The coroutine context to use for executing the block. Defaults to [EmptyCoroutineContext].
 * @param block The suspend block of code to execute.
 * @return A [Result] containing either the result of the block or the exception that occurred.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("RedundantSuspendModifier")
suspend inline fun <R> runSuspendCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> R,
): Result<R> {
    // This contract ensures that the block is called exactly once.
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    // Try to execute the block and catch any exceptions that occur.
    return try {
        Result.success(withContext(context) { block() })
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}