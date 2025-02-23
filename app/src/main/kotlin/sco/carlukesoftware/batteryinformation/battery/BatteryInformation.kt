package sco.carlukesoftware.batteryinformation.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

/**
 * Create the battery level flow
 *
 * @param context The context
 */
fun getBatteryLevelFlow(context: Context): Flow<Int> {
    return callbackFlow {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val receiver = context.registerReceiver(null, intentFilter)

        val level = receiver?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = receiver?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPercentage = (level * 100 / scale.toFloat()).toInt()
        trySend(batteryPercentage)

        awaitClose {
            // No need to unregister the receiver here, as it's a sticky broadcast
        }
    }.map { batteryPercentage ->
        batteryPercentage
    }
}

fun getPowerConnectedFlow(context: Context): Flow<Boolean> {
    return callbackFlow {
        val intentFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        val receiver = context.registerReceiver(null, intentFilter)

        val isPowerConnected = receiver != null
        trySend(isPowerConnected)
        awaitClose {
            // No need to unregister the receiver here, as it's a sticky broadcast
        }
    }.map { isPowerConnected ->
        isPowerConnected
    }
}
