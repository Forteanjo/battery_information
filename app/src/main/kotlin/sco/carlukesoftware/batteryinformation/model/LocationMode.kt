package sco.carlukesoftware.batteryinformation.model

import android.os.PowerManager

enum class LocationMode(val description: String) {

    NO_CHANGE("No Change"),
    GPS_DISABLED_WHEN_SCREEN_OFF("GPS Disabled When Screen Off"),
    ALL_DISABLED_WHEN_SCREEN_OFF("All Disabled When Screen Off"),
    FOREGROUND_ONLY("Foreground Only"),
    THROTTLE_REQUESTS_WHEN_SCREEN_OFF("Throttle Request When Screen Off"),
    UNKNOWN("Unknown");

    companion object {
        fun fromLocationMode(locationMode: Int): LocationMode =
            when (locationMode) {
                PowerManager.LOCATION_MODE_NO_CHANGE -> NO_CHANGE
                PowerManager.LOCATION_MODE_GPS_DISABLED_WHEN_SCREEN_OFF -> GPS_DISABLED_WHEN_SCREEN_OFF
                PowerManager.LOCATION_MODE_ALL_DISABLED_WHEN_SCREEN_OFF -> ALL_DISABLED_WHEN_SCREEN_OFF
                PowerManager.LOCATION_MODE_FOREGROUND_ONLY -> FOREGROUND_ONLY
                PowerManager.LOCATION_MODE_THROTTLE_REQUESTS_WHEN_SCREEN_OFF -> THROTTLE_REQUESTS_WHEN_SCREEN_OFF
                else -> UNKNOWN
            }
    }
}
