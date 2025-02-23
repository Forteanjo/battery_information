package sco.carlukesoftware.batteryinformation.ui.components

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import sco.carlukesoftware.batteryinformation.battery.BatteryBroadcastReceiver
import sco.carlukesoftware.batteryinformation.model.BatteryInformation

@Composable
fun BatteryInfoSystem() {
    val context = LocalContext.current
    var batteryInformation by remember {
        mutableStateOf(BatteryInformation())
    }

    val batteryInfoFlow: Flow<BatteryInformation> =
        remember {
            callbackFlow {
            val receiver = BatteryBroadcastReceiver { info ->
                trySend(info)
            }

            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
                addAction(Intent.ACTION_BATTERY_LOW)
                addAction(Intent.ACTION_BATTERY_OKAY)
                addAction(Intent.ACTION_BATTERY_CHANGED)
            }

            ContextCompat.registerReceiver(context, receiver, filter,
                ContextCompat.RECEIVER_EXPORTED)

            awaitClose {
                context.unregisterReceiver(receiver)
            }
        }
    }

    LaunchedEffect(Unit) {
        batteryInfoFlow.collectLatest { info ->
            batteryInformation = info
        }
    }



    Column {
        Text("Battery Level: ${batteryInformation.level}%")
        Text("Charging: ${batteryInformation.isCharging}")
        Text("USB Charging: ${batteryInformation.isUsbCharging}")
        Text("AC Charging: ${batteryInformation.isAcCharging}")
        Text("Temperature: ${batteryInformation.temperature}°C")
        Text("Voltage: ${batteryInformation.voltage}V")
        Text("Technology: ${batteryInformation.technology}")
        Text("Power Connected: ${batteryInformation.isPowerConnected}")
        Text("Low Battery: ${batteryInformation.isLowBattery}")
    }
}
