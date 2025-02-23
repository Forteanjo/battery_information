package sco.carlukesoftware.batteryinformation.model

data class BatteryInformation(
    val isPowerConnected: Boolean = false,
    val isLowBattery: Boolean = false,
    val level: Int = 0,
    val isCharging: Boolean = false,
    val isUsbCharging: Boolean = false,
    val isAcCharging: Boolean = false,
    val temperature: Float = 0f,
    val voltage: Int = 0,
    val technology: String = "<Unknown>"
)
