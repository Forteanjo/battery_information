package sco.carlukesoftware.batteryinformation.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager

/**
 * A [BroadcastReceiver] that listens for screen on/off events.
 *
 * This receiver is designed to be registered with the system to be notified
 * when the screen state changes (i.e., when the screen is turned on or off).
 * When a screen on/off event is received, it invokes the provided
 * `onValueChange` callback.
 *
 * @property actionScreenChange A lambda function that is invoked when a screen on/off
 *                         event is received. This function is typically used to
 *                         perform actions based on the screen state change.
 */
class ScreenOnOffReceiver(
    private val actionScreenChange: (Boolean) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        actionScreenChange(intent?.action == Intent.ACTION_SCREEN_ON)
    }
}
