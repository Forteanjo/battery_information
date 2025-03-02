package sco.carlukesoftware.batteryinformation.utils

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

/**
 * Converts a listener-based API to a Flow.
 *
 * @param registerListener A lambda that takes the listener instance and registers it with the API.
 * @param unregisterListener A lambda that unregisters the listener.
 * @param createListener A lambda that creates the listener instance.
 * @param T The type of data emitted by the Flow.
 * @return A Flow that emits values from the listener.
 */
fun <T> listenerToFlow(
    registerListener: (listener: T) -> Unit,
    unregisterListener: (listener: T) -> Unit,
    createListener: (sendValue: (T) -> Unit) -> T
): Flow<T> = callbackFlow {
    // Create the listener instance
    val listener = createListener { value ->
        // Emit the value to the flow
        trySend(value).isSuccess
    }

    // Register the listener
    registerListener(listener)

    // Unregister the listener when the flow is cancelled
    awaitClose {
        unregisterListener(listener)
    }
}.conflate()
