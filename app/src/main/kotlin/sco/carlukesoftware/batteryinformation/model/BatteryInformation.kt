package sco.carlukesoftware.batteryinformation.model

/**
 * Data class representing information about the device's battery.
 *
 * This class encapsulates various battery-related details such as power connection status,
 * battery level, charging status, health, plugged type, low battery indication, temperature,
 * voltage, and technology.
 *
 * @property isPowerConnected `true` if the device is connected to a power source, `false` otherwise.
 * @property level The current battery level as a percentage (0-100).
 * @property scale The maximum battery level (usually 100).
 * @property chargingStatus The current charging status of the battery (e.g., charging, discharging, full, unknown).
 * @property batteryHealth The health status of the battery (e.g., good, overheat, dead, unknown).
 * @property pluggedType The type of power source the device is plugged into (e.g., AC, USB, wireless, unknown).
 * @property isBatteryLow `true` if the battery is considered low, `false` otherwise.
 * @property temperatureCelsius The battery temperature in degrees Celsius.
 * @property voltageMillivolts The battery voltage in millivolts.
 * @property technology The battery technology (e.g., Li-ion, NiMH, unknown).
 * @property present `true` if the battery is present, `false` otherwise.
 * @property capacity battery capacity as an integer percentage of total capacity.
 * @property chargeCounter battery capacity in microampere-hours, as an integer.
 * @property energyCounter battery capacity in kilowatthours, as a long.
 * @property currentAverage current average in microamperes, as an integer.
 * @property currentNow current now in microamperes, as an integer.
 * @property chargeTimeRemaining charge time remaining in milliseconds, as a long.
 */
data class BatteryInformation(
    val isPowerConnected: Boolean = false,
    val level: Int = 0,
    val scale: Int = 100,
    val chargingStatus: ChargingStatus = ChargingStatus.UNKNOWN,
    val batteryHealth: BatteryHealth = BatteryHealth.UNKNOWN,
    val pluggedType: PluggedType = PluggedType.UNKNOWN,
    val isBatteryLow: Boolean = false,
    val temperatureCelsius: Float = 0f,
    val voltageMillivolts: Int = 0,
    val technology: String = "<Unknown>",
    val present: Boolean = false,
    val batteryProperties: BatteryProperties = BatteryProperties()
) {
    val isFullCharge: Boolean
        get() = level == 100

    val isCharging: Boolean
        get() = chargingStatus == ChargingStatus.CHARGING

    val isDischarging: Boolean
        get() = chargingStatus == ChargingStatus.DISCHARGING

}
