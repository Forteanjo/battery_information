package sco.carlukesoftware.batteryinformation.model

import android.os.PowerManager

enum class ThermalStatus(val description: String) {
    NONE("None"),
    LIGHT("Light"),
    MODERATE("Moderate"),
    SEVERE("Severe"),
    CRITICAL("Critical"),
    EMERGENCY("Emergency"),
    SHUTDOWN("Shutdown"),
    UNKNOWN("Unknown");

    companion object {
        fun fromThermalStatus(thermalStatus: Int): ThermalStatus =
            when (thermalStatus) {
                PowerManager.THERMAL_STATUS_NONE -> NONE
                PowerManager.THERMAL_STATUS_LIGHT -> LIGHT
                PowerManager.THERMAL_STATUS_MODERATE -> MODERATE
                PowerManager.THERMAL_STATUS_SEVERE -> SEVERE
                PowerManager.THERMAL_STATUS_CRITICAL -> CRITICAL
                PowerManager.THERMAL_STATUS_EMERGENCY -> EMERGENCY
                PowerManager.THERMAL_STATUS_SHUTDOWN -> SHUTDOWN
                else -> UNKNOWN
            }
    }
}
