package sco.carlukesoftware.batteryinformation.ui.screens

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import sco.carlukesoftware.batteryinformation.R
import sco.carlukesoftware.batteryinformation.battery.BatteryBroadcastReceiver
import sco.carlukesoftware.batteryinformation.model.BatteryInformation
import sco.carlukesoftware.batteryinformation.ui.components.BatteryInfoComponent
import sco.carlukesoftware.batteryinformation.ui.components.EmbossedText
import sco.carlukesoftware.batteryinformation.ui.components.MoonToSunSwitcher
import sco.carlukesoftware.batteryinformation.ui.components.PercentageLevel
import sco.carlukesoftware.batteryinformation.ui.theme.BatteryInformationTheme
import sco.carlukesoftware.batteryinformation.ui.theme.Shapes
import sco.carlukesoftware.batteryinformation.utils.brushedMetal
import sco.carlukesoftware.batteryinformation.utils.drawAnimationBorder


@Composable
fun BatteryInfoScreen(
    modifier: Modifier = Modifier,
    onDarkModeChanged: (Boolean) -> Unit = {}
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

    var isMoon by remember {
        mutableStateOf(false)
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

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = Shapes.large
                    )
                    .brushedMetal(),
            ) {
                val (title, switcher) = createRefs()

                EmbossedText(
                    text = "Battery Status",
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(
                                anchor = parent.top,
                                margin = 8.dp
                            )
                            bottom.linkTo(
                                anchor = parent.bottom,
                                margin = 8.dp
                            )
                            start.linkTo(
                                anchor = parent.start,
                                margin = 8.dp
                            )
                        },
                    style = MaterialTheme.typography.headlineLarge
                )


                MoonToSunSwitcher(
                    isMoon = isMoon,
                    color = if (isMoon) Color.White else Color.Yellow,
                    modifier = Modifier
                        .constrainAs(switcher) {
                            top.linkTo(
                                anchor = parent.top,
                                margin = 8.dp
                            )
                            bottom.linkTo(
                                anchor = parent.bottom,
                                margin = 8.dp
                            )
                            end.linkTo(
                                anchor = parent.end,
                                margin = 8.dp
                            )
                        }
                        .clickable {
                            isMoon = !isMoon
                            onDarkModeChanged(isMoon)
                        }
                )
            }

            val circleSize = 300.dp

            PercentageLevel(
                percent = batteryInformation.level,
                modifier = Modifier
                    .width(circleSize)
                    .height(circleSize)
                    .padding(16.dp)
                    .align(
                        alignment = Alignment.CenterHorizontally
                    )
                    .drawAnimationBorder(
                        strokeWidth = 18.dp,
                        shape = RoundedCornerShape(circleSize),
                        enabled = batteryInformation.isCharging,
                    )            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BatteryInfoComponent(
                    modifier = Modifier
                        .padding(
                            horizontal = 4.dp
                        )
                        .height(96.dp)
                        .fillMaxWidth(0.5f)
                        .clip(
                            shape = Shapes.large
                        )
                        .brushedMetal(),
                    imageRes = R.drawable.bolt,
                    header = "Voltage",
                    value = "${batteryInformation.voltageMillivolts}V"
                )

                BatteryInfoComponent(
                    modifier = Modifier
                        .padding(
                            horizontal = 4.dp
                        )
                        .height(96.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = Shapes.large
                        )
                        .brushedMetal(),
                    imageRes = R.drawable.thermometer,
                    header = "Temperature",
                    value = "${batteryInformation.temperatureCelsius}°C"
                )
            }

            Spacer(
                modifier = Modifier
                    .height(2.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BatteryInfoComponent(
                    modifier = Modifier
                        .padding(
                            horizontal = 4.dp
                        )
                        .height(96.dp)
                        .fillMaxWidth(0.5f)
                        .clip(
                            shape = Shapes.large
                        )
                        .brushedMetal(),
                    imageRes = R.drawable.technology,
                    header = "Technology",
                    value = batteryInformation.technology,
                    valueTextStyle = MaterialTheme.typography.headlineMedium
                )


                BatteryInfoComponent(
                    modifier = Modifier
                        .padding(
                            horizontal = 4.dp
                        )
                        .height(96.dp)
                        .fillMaxWidth()
                        .clip(
                            shape = Shapes.large
                        )
                        .brushedMetal(),
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
                    Text("Plugged type: ${batteryInformation.pluggedType.description}")
                    Text("Temperature: ${batteryInformation.temperatureCelsius}°C")
                    Text("Voltage: ${batteryInformation.voltageMillivolts}V")
                    Text("Charging Status: ${batteryInformation.chargingStatus.description}")
                    Text("Battery Health: ${batteryInformation.batteryHealth.description}")
                    Text("Technology: ${batteryInformation.technology}")
                    Text("Power Connected: ${batteryInformation.isPowerConnected}")
                    Text("Low Battery: ${batteryInformation.isBatteryLow}")
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
