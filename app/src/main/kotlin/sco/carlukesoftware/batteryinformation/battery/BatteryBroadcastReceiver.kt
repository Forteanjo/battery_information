package sco.carlukesoftware.batteryinformation.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import sco.carlukesoftware.batteryinformation.model.BatteryInformation

class BatteryBroadcastReceiver(
    private val onBatteryInfoChanged: (BatteryInformation) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                onBatteryInfoChanged(
                    BatteryInformation(
                        isPowerConnected = true
                    )
                )
            }

            Intent.ACTION_POWER_DISCONNECTED -> {
                onBatteryInfoChanged(
                    BatteryInformation(
                        isPowerConnected = false
                    )
                )
            }

            Intent.ACTION_BATTERY_LOW -> {
                onBatteryInfoChanged(
                    BatteryInformation(
                        isLowBattery = true
                    )
                )
            }

            Intent.ACTION_BATTERY_OKAY -> {
                onBatteryInfoChanged(
                    BatteryInformation(
                        isLowBattery = false
                    )
                )
            }

            Intent.ACTION_BATTERY_CHANGED -> {
                intent.let {
                    val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                    val batteryStatus = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    val isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                            batteryStatus == BatteryManager.BATTERY_STATUS_FULL
                    val chargePlug = it.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                    val isUsbCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
                    val isAcCharging = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
                    val temperature = it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).div(10f)
                    val voltage = it.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0).div(1000)
                    val technology =
                        it.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "<Unknown>"

                    onBatteryInfoChanged(
                        BatteryInformation(
                            isPowerConnected = isCharging || isUsbCharging || isAcCharging,
                            level = level,
                            isCharging = isCharging,
                            isUsbCharging = isUsbCharging,
                            isAcCharging = isAcCharging,
                            temperature = temperature,
                            voltage = voltage,
                            technology = technology
                        )
                    )
                }
            }
        }
    }
}
