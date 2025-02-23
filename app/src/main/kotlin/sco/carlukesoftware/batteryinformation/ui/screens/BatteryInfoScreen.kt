package sco.carlukesoftware.batteryinformation.ui.screens

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import sco.carlukesoftware.batteryinformation.R
import sco.carlukesoftware.batteryinformation.battery.BatteryBroadcastReceiver
import sco.carlukesoftware.batteryinformation.model.BatteryInformation
import sco.carlukesoftware.batteryinformation.ui.components.AnimatedWaveCircle
import sco.carlukesoftware.batteryinformation.ui.components.BatteryInfoComponent
import sco.carlukesoftware.batteryinformation.ui.components.EmbossedText
import sco.carlukesoftware.batteryinformation.ui.theme.BatteryInformationTheme
import sco.carlukesoftware.batteryinformation.ui.theme.Rose100
import sco.carlukesoftware.batteryinformation.utils.brushedMetal
import sco.carlukesoftware.batteryinformation.utils.drawAnimationBorder

@Composable
fun BatteryInfoScreen(
    modifier: Modifier = Modifier
) {
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val verticalLayout by remember {
            derivedStateOf {
                maxHeight > maxWidth
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .brushedMetal(),
                verticalAlignment = Alignment
                    .CenterVertically
            ) {
                EmbossedText(
                    text = "Battery Status",
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        ),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            AnimatedWaveCircle(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .padding(16.dp)
                    .drawAnimationBorder(
                        strokeWidth = 18.dp,
                        shape = RoundedCornerShape(100.dp),
                        enabled = batteryInformation.isCharging,
                    ),
                waterLevel = batteryInformation.level / 100f,
                waveColor = Color.Red,
                circleColor = Rose100
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1 / 3f)
            ) {
                BatteryInfoComponent(
                    modifier = Modifier
                        .height(96.dp)
                        .fillMaxWidth(0.5f),
                    imageRes = R.drawable.bolt,
                    header = "Voltage",
                    value = "${batteryInformation.voltage}V"
                )

                BatteryInfoComponent(
                    modifier = Modifier
                        .height(96.dp)
                        .fillMaxWidth(),
                    imageRes = R.drawable.thermometer,
                    header = "Temperature",
                    value = "${batteryInformation.temperature}°C"
                )
            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1 / 2f)
            ) {
                BatteryInfoComponent(
                    modifier = Modifier
                        .height(96.dp)
                        .fillMaxWidth(0.5f),
                    imageRes = R.drawable.technology,
                    header = "Technology",
                    value = batteryInformation.technology,
                    valueTextStyle = MaterialTheme.typography.headlineMedium
                )


                BatteryInfoComponent(
                    modifier = Modifier
                        .height(96.dp)
                        .fillMaxWidth(),
                    imageRes = R.drawable.plugin,
                    header = "Plug status",
                    value = if (batteryInformation.isPowerConnected) "plugged-in" else "unplugged",
                    valueTextStyle = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Text("Battery Level: ${batteryInformation.level}%")
                    Text("Charging: ${batteryInformation.isCharging}")
                    Text("USB Charging: ${batteryInformation.isCharging && batteryInformation.isUsbCharging}")
                    Text("AC Charging: ${batteryInformation.isCharging && batteryInformation.isAcCharging}")
                    Text("Temperature: ${batteryInformation.temperature}°C")
                    Text("Voltage: ${batteryInformation.voltage}V")
                    Text("Technology: ${batteryInformation.technology}")
                    Text("Power Connected: ${batteryInformation.isPowerConnected}")
                    Text("Low Battery: ${batteryInformation.isLowBattery}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BatterInfoScreenPreview() {
    BatteryInformationTheme(darkTheme = true) {
        BatteryInfoScreen()
    }
}
