package sco.carlukesoftware.batteryinformation.ui.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@Composable
fun ObserveSystemBroadcast(
    systemAction: String,
    onReceive: (intent: Intent?) -> Unit
) {
    val context = LocalContext.current

    val currentOnReceive by rememberUpdatedState(onReceive)

    // Use a LaunchedEffect to handle the broadcast
    // receiver registration and unregistration
    LaunchedEffect(context, systemAction){
        val broadcastFlow = callbackFlow {
            val intentFilter = IntentFilter(systemAction)

            val receiver = object : BroadcastReceiver(){
                override fun onReceive(context: Context?, intent: Intent?) {
                    intent?.let {
                        trySend(intent) // Send the intent to the flow
                    }
                }
            }

            context.registerReceiver(receiver, intentFilter)
            awaitClose {
                context.unregisterReceiver(receiver)
            }
        }

        broadcastFlow.collect { intent ->
            currentOnReceive(intent)
        }
    }
}
