package sco.carlukesoftware.batteryinformation.model

import android.os.BatteryManager

/**
 * Represents the type of power source the device is currently plugged into.
 *
 * This enum class provides a type-safe way to represent the different
 * types of power sources that a device can be connected to. It includes
 * common types like AC, USB, Wireless, and Dock, as well as an
 * UNKNOWN state for cases where the type cannot be determined.
 *
 * Each enum constant has a corresponding description string, which can be
 * used for display purposes.
 *
 * @property description A human-readable description of the plugged type.
 */
enum class PluggedType(val description: String) {
    AC("AC"),
    USB("USB"),
    WIRELESS("Wireless"),
    DOCK("Docked"),
    UNKNOWN("Unknown");

    companion object {
        /**
         * Represents the type of power source the device is plugged into.
         */
        fun fromPluggedType(pluggedType: Int): PluggedType =
            when (pluggedType) {
                BatteryManager.BATTERY_PLUGGED_AC -> AC
                BatteryManager.BATTERY_PLUGGED_USB -> USB
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> WIRELESS
                BatteryManager.BATTERY_PLUGGED_DOCK -> DOCK
                else -> UNKNOWN
            }
    }
}
