package sco.carlukesoftware.batteryinformation.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Registers a BroadcastReceiver and returns a Flow that emits values based on received broadcasts.
 *
 * @param context The application context.
 * @param filter The IntentFilter to use for the receiver.
 * @param receiverCreator A lambda to create the BroadcastReceiver.
 * @param T The type of data emitted by the Flow.
 * @return A Flow that emits values of type T.
 */
fun <T> registerReceiverFlow(
    context: Context,
    filter: IntentFilter,
    receiverCreator: (send: (T) -> Unit) -> BroadcastReceiver
): Flow<T> = callbackFlow {
    val receiver = receiverCreator { value ->
        trySend(value)
    }

    ContextCompat.registerReceiver(
        context,
        receiver,
        filter,
        ContextCompat.RECEIVER_EXPORTED
    )

    awaitClose {
        context.unregisterReceiver(receiver)
    }
}
