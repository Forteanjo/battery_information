package sco.carlukesoftware.batteryinformation.model

import android.os.BatteryManager

/**
 * Represents the charging status of a device's battery.
 *
 * This enum provides a type-safe way to represent the different states
 * a device's battery can be in, such as charging, discharging, full,
 * not charging, or unknown. Each enum value has a corresponding
 * human-readable description.
 *
 * @property description A human-readable description of the charging status.
 */
enum class ChargingStatus(val description: String) {
    CHARGING("Charging"),
    DISCHARGING("Discharging"),
    FULL("Full"),
    NOT_CHARGING("Not charging"),
    UNKNOWN("Unknown");

    companion object {
        /**
         * Converts an integer representation of battery charging status from BatteryManager
         * to a corresponding ChargingStatus enum value.
         *
         * @param status The integer status code from BatteryManager, such as
         *               BatteryManager.BATTERY_STATUS_CHARGING.
         * @return The corresponding ChargingStatus enum value.
         *         - CHARGING if status is BatteryManager.BATTERY_STATUS_CHARGING.
         *         - DISCHARGING if status is BatteryManager.BATTERY_STATUS_DISCHARGING.
         *         - FULL if status is BatteryManager.BATTERY_STATUS_FULL.
         *         - NOT_CHARGING if status is BatteryManager.BATTERY_STATUS_NOT_CHARGING.
         *         - UNKNOWN if the status code is not recognized.
         */
        fun fromChargingStatus(status: Int): ChargingStatus =
            when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> CHARGING
                BatteryManager.BATTERY_STATUS_DISCHARGING -> DISCHARGING
                BatteryManager.BATTERY_STATUS_FULL -> FULL
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> NOT_CHARGING
                else -> UNKNOWN
            }
    }
}
