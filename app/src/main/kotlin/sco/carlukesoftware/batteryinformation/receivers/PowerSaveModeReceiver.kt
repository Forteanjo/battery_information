package sco.carlukesoftware.batteryinformation.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager

/**
 * A [BroadcastReceiver] that listens for changes in the device's power save mode status.
 *
 * This receiver is designed to be used with the `android.os.PowerManager.ACTION_POWER_SAVE_MODE_CHANGED`
 * intent action. When the power save mode status changes, it triggers a callback function.
 *
 * @property isPowerSaveMode A lambda function that is called when the power save mode status changes.
 *                         This function should contain the logic to execute when a change is detected.
 */
class PowerSaveModeReceiver(
    private val onPowerSaveModeChange: (Boolean) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val isPowerSaveMode = context?.powerManager?.isPowerSaveMode ?: false
        onPowerSaveModeChange(isPowerSaveMode)
    }

    private inline val Context.powerManager
        @SuppressLint("InlinedApi")
        get() = getSystemService(Context.POWER_SERVICE) as PowerManager

}
