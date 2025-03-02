package sco.carlukesoftware.batteryinformation.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager

/**
 * A BroadcastReceiver that listens for changes in the device's idle mode status.
 *
 * This receiver is designed to be registered to listen for the
 * [PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED] broadcast action. When the device
 * enters or exits idle mode, the [onValueChange] callback is invoked.
 *
 * @property onIdleModeChange A lambda function that is called when the device's idle mode
 *                          status changes. This function should handle the logic necessary
 *                          to react to the change in idle mode.
 */
class DeviceIdleModeReceiver(
    private val onIdleModeChange: (Boolean) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val isIdling = context?.powerManager?.isDeviceIdleMode ?: false
        onIdleModeChange(isIdling)
    }

    private inline val Context.powerManager
        @SuppressLint("InlinedApi")
        get() = getSystemService(Context.POWER_SERVICE) as PowerManager

}
