package sco.carlukesoftware.batteryinformation.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.RequiresApi
import sco.carlukesoftware.batteryinformation.model.BatteryHealth
import sco.carlukesoftware.batteryinformation.model.BatteryInformation
import sco.carlukesoftware.batteryinformation.model.ChargingStatus
import sco.carlukesoftware.batteryinformation.model.PluggedType

/**
 * A BroadcastReceiver that listens for battery-related events and provides updated
 * [BatteryInformation] through a callback.
 *
 * This class registers for various battery-related actions, including:
 * - Power connected/disconnected ([Intent.ACTION_POWER_CONNECTED], [Intent.ACTION_POWER_DISCONNECTED])
 * - Battery low ([Intent.ACTION_BATTERY_LOW])
 * - Battery information changes ([Intent.ACTION_BATTERY_CHANGED])
 *
 * When an event is received, it extracts relevant battery data from the intent
 * and calls the provided `onBatteryInfoChanged` callback with a [BatteryInformation]
 * object containing the latest information.
 *
 * @property onBatteryInfoChanged A callback function that receives the updated [BatteryInformation]
 *                                 whenever a battery-related event occurs.
 */
class BatteryBroadcastReceiver(
    private val onBatteryInfoChanged: (BatteryInformation) -> Unit
) : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                        isBatteryLow = true
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
                    val isBatteryLow = it.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false)
                    val temperature = it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).div(10f)
                    val voltage = it.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0).div(1000)
                    val chargingStatus = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    val batteryHealth = it.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
                    val technology =
                        it.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "<Unknown>"
                    //val chargingTimeRemaining = BatteryManager.computeChargeTimeRemaining()

                    onBatteryInfoChanged(
                        BatteryInformation(
                            isPowerConnected = isCharging || isUsbCharging || isAcCharging,
                            level = level,
                            pluggedType = PluggedType.fromPluggedType(chargePlug),
                            isBatteryLow = isBatteryLow,
                            temperatureCelsius = temperature,
                            voltageMillivolts = voltage,
                            chargingStatus = ChargingStatus.fromChargingStatus(chargingStatus),
                            batteryHealth = BatteryHealth.fromBatteryHealth(batteryHealth),
                            technology = technology
                        )
                    )
                }
            }
        }
    }
}
