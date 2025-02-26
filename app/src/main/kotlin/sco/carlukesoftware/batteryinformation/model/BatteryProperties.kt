package sco.carlukesoftware.batteryinformation.model

import android.annotation.SuppressLint
import kotlin.text.format

/**
 * Data class representing specific battery properties.
 */
data class BatteryProperties(
    val capacity: Int = -1,
    val chargeCounter: Int = -1,
    val energyCounter: Long = -1,
    val currentAverage: Int = -1,
    val currentNow: Int = -1,
    val chargeTimeRemaining: Long = -1L
) {

    val chargingTimeLeft: String
        get() {
            // Breakdown our milliseconds
            val seconds = (chargeTimeRemaining / MILLISECONDS_IN_SECOND).toInt()
            val hours = seconds / SECONDS_IN_HOUR
            val minutes = (seconds % SECONDS_IN_HOUR)/ MINUTES_IN_HOUR
            val secondsLeft = seconds % SECONDS_IN_MINUTE
            val millisecondsLeft = chargeTimeRemaining % MILLISECONDS_IN_SECOND

            var result = ""
            if (hours > 0) {
                result += "$hours h"
            }
            if (minutes > 0) {
                result += " $minutes min"
            }
            if (secondsLeft > 0) {
                result += " $secondsLeft sec"
            }
            return result.trim()
        }


    /**
     * Returns the energy counter in a human-friendly format (mAh or Ah).
     */
    val energyCounterString: String
        @SuppressLint("DefaultLocale")
        get() {
            val energyCounterAh = energyCounter.toDouble() / ENERGY_AMPERE_HOURS
            val energyCounterMah = energyCounter.toDouble() / ENERGY_MILLI_AMPERE_HOURS

            return if (energyCounterAh >= 1.0) {
                String.format("%.2f Ah", energyCounterAh)
            } else {
                String.format("%.2f mAh", energyCounterMah)
            }
        }

    /**
     * Returns the charge counter in a human-friendly format (mAh or Ah).
     */
    val chargeCounterString: String
        @SuppressLint("DefaultLocale")
        get() {
            val chargeCounterAh = chargeCounter.toDouble() / CHARGE_AMPERE_HOURS
            val chargeCounterMah = chargeCounter.toDouble() / CHARGE_MILLI_AMPERE_HOURS

            return if (chargeCounterAh >= 1.0) {
                String.format("%.2f Ah", chargeCounterAh)
            } else {
                String.format("%.2f mAh", chargeCounterMah)
            }
        }

    companion object {
        const val CHARGE_AMPERE_HOURS = 1_000_000.0
        const val CHARGE_MILLI_AMPERE_HOURS = 1_000.0
        const val ENERGY_AMPERE_HOURS = 1_000_000_000.0
        const val ENERGY_MILLI_AMPERE_HOURS = 1_000_000.0

        const val MILLISECONDS_IN_SECOND = 1000
        const val SECONDS_IN_MINUTE = 60
        const val MINUTES_IN_HOUR = 60
        const val SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR

    }
}
