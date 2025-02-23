package sco.carlukesoftware.batteryinformation.model

import android.os.BatteryManager

/**
 * Represents the health status of a battery.
 *
 * This enum provides a human-readable description for each battery health state as defined by
 * `android.os.BatteryManager`. It also includes a companion object to facilitate easy
 * conversion from the integer constants defined in `BatteryManager` to the corresponding
 * `BatteryHealth` enum values.
 *
 * @property description A human-readable description of the battery health.
 */
enum class BatteryHealth(val description: String) {

    GOOD("Good"),
    COLD("Cold"),
    DEAD("Dead"),
    OVERHEAT("Overheat"),
    OVER_VOLTAGE("Over voltage"),
    UNSPECIFIED_FAILURE("Unspecified failure"),
    UNKNOWN("Unknown");

    companion object {

        /**
         * Represents the health status of the battery.
         */
        fun fromBatteryHealth(health: Int): BatteryHealth =
            when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> GOOD
                BatteryManager.BATTERY_HEALTH_COLD -> COLD
                BatteryManager.BATTERY_HEALTH_DEAD -> DEAD
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> OVERHEAT
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> OVER_VOLTAGE
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> UNSPECIFIED_FAILURE
                else -> UNKNOWN
            }

    }
}
