package sco.carlukesoftware.batteryinformation.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.PowerManager
import android.os.PowerManager.THERMAL_STATUS_CRITICAL
import android.os.PowerManager.THERMAL_STATUS_EMERGENCY
import android.os.PowerManager.THERMAL_STATUS_LIGHT
import android.os.PowerManager.THERMAL_STATUS_MODERATE
import android.os.PowerManager.THERMAL_STATUS_NONE
import android.os.PowerManager.THERMAL_STATUS_SEVERE
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import sco.carlukesoftware.batteryinformation.utils.registerReceiverFlow

/**
 * [PowerManagerViewModel] is a ViewModel class that provides information about the device's power state.
 * It exposes [StateFlow]s that reflect the current status of:
 * - Device idle mode ([isDeviceIdle]).
 * - Power save mode ([isPowerSaveMode]).
 * - Screen on/off state ([isScreenOn]).
 *
 * This ViewModel utilizes [BroadcastReceiver]s and [Flow]s to listen for system events related to power management.
 *
 * @param application The application instance.
 */
@RequiresApi(Build.VERSION_CODES.Q)
class PowerManagerViewModel(
    application: Application,
    private val ioDispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {

    private val _isDeviceIdle = MutableStateFlow(false)
    val isDeviceIdle: StateFlow<Boolean> = _isDeviceIdle.asStateFlow()

    private val _isPowerSaveMode = MutableStateFlow(false)
    val isPowerSaveMode: StateFlow<Boolean> = _isPowerSaveMode.asStateFlow()

    private val _isScreenOn = MutableStateFlow(false)
    val isScreenOn: StateFlow<Boolean> = _isScreenOn.asStateFlow()

    private val _thermalStatus = MutableStateFlow(0)
    val thermalStatus: StateFlow<Int> = _thermalStatus.asStateFlow()

    init {
        viewModelScope.launch {
            deviceIdleFlow(application).collect {
                _isDeviceIdle.value = it
            }

            powerSaveModeFlow(application).collect {
                _isPowerSaveMode.value = it
            }

            screenOnOffFlow(application).collect {
                _isScreenOn.value = it
            }

            listenThermalStatus(application)
        }
    }

    /**
     * Creates a Flow that emits `true` when the device enters idle mode and `false` when it exits.
     *
     * @param context The application context.
     * @return A Flow of Boolean values indicating device idle state.
     */
    private fun deviceIdleFlow(context: Context): Flow<Boolean> =
        registerReceiverFlow(
            context = context,
            filter = IntentFilter(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED)
        ) { send ->
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Log.d("deviceIdleFlow", "onReceive: ${context.powerManager?.isDeviceIdleMode}")
                    send(context.powerManager?.isDeviceIdleMode?: false)
                }
            }
        }

    /**
     * Creates a Flow that emits `true` when power save mode is enabled and `false` when it's disabled.
     *
     * @param context The application context.
     * @return A Flow of Boolean values indicating power save mode state.
     */
    private fun powerSaveModeFlow(context: Context): Flow<Boolean> =
        registerReceiverFlow(
            context = context,
            filter = IntentFilter(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
        ) { send ->
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Log.d("powerSaveModeFlow", "onReceive: ${context.powerManager?.isPowerSaveMode}")
                    send(context.powerManager?.isPowerSaveMode?: false)
                }
            }
        }

    /**
     * Creates a Flow that emits `true` when the screen turns on and `false` when it turns off.
     *
     * @param context The application context.
     * @return A Flow of Boolean values indicating screen on/off state.
     */
    private fun screenOnOffFlow(context: Context): Flow<Boolean> =
        registerReceiverFlow(
            context,
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
            }
        ) { send ->
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Log.d("screenOnOffFlow", "onReceive: ${intent.action == Intent.ACTION_SCREEN_ON}")
                    send(intent.action == Intent.ACTION_SCREEN_ON)
                }
            }
        }

    /**
     * Listens for changes in the device's thermal status and logs them.
     *
     * This function uses a coroutine to collect thermal status updates from a flow provided by
     * [Context.getThermalStatusFlow()]. It then logs the received thermal status to the console.
     *
     * This function requires Android Q (API level 29) or higher.
     *
     * @param context The application context, used to access the thermal status flow.
     *
     * @throws IllegalArgumentException If the provided context is invalid or null.
     * @throws IllegalStateException If the thermal status flow is not available.
     *
     * **Thermal Status Levels:**
     * - [THERMAL_STATUS_NONE]: No thermal throttling.
     * - [THERMAL_STATUS_LIGHT]: Light thermal throttling.
     * - [THERMAL_STATUS_MODERATE]: Moderate thermal throttling.
     * - [THERMAL_STATUS_SEVERE]: Severe thermal throttling.
     * - [THERMAL_STATUS_CRITICAL]: Critical thermal throttling.
     * - [THERMAL_STATUS_EMERGENCY]: Emergency thermal throttling.
     * - Other values: indicates unknown thermal status.
     *
     * **Example Usage:**
     * ```kotlin
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
     *     listenThermalStatus(applicationContext)
     * }
     * ```
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun listenThermalStatus(context: Context) {
        CoroutineScope(ioDispatcher).launch {
            context.getThermalStatusFlow()
                .collect { status ->
                    _thermalStatus.value = status
                    when (status) {
                        THERMAL_STATUS_NONE -> Log.d("listenThermalStatus", "Thermal status: NONE")
                        THERMAL_STATUS_LIGHT -> Log.d("listenThermalStatus", "Thermal status: LIGHT")
                        THERMAL_STATUS_MODERATE -> Log.d("listenThermalStatus", "Thermal status: MODERATE")
                        THERMAL_STATUS_SEVERE -> Log.d("listenThermalStatus", "Thermal status: SEVERE")
                        THERMAL_STATUS_CRITICAL -> Log.d("listenThermalStatus", "Thermal status: CRITICAL")
                        THERMAL_STATUS_EMERGENCY -> Log.d("listenThermalStatus", "Thermal status: EMERGENCY")
                        else -> Log.d("listenThermalStatus", "Unknown thermal status: $status")
                    }
                }
        }
    }


    /**
     * Retrieves the thermal status of the device as a Flow.
     *
     * This function provides a stream of thermal status updates from the system.
     * It uses a callback mechanism to listen for changes in the thermal status
     * and emits the updated status to the Flow. It also emits the initial
     * thermal status upon subscription.
     *
     * **Note:** This function is only available on devices running Android Q (API level 29) or higher.
     *
     * @receiver Context The context used to access the PowerManager.
     * @return Flow<Int> A Flow emitting the current thermal status as an integer.
     *                  The possible values are defined in [PowerManager]:
     *                  - [PowerManager.THERMAL_STATUS_NONE]: No throttling is occurring.
     *                  - [PowerManager.THERMAL_STATUS_LIGHT]: Mild throttling is occurring.
     *                  - [PowerManager.THERMAL_STATUS_MODERATE]: Moderate throttling is occurring.
     *                  - [PowerManager.THERMAL_STATUS_SEVERE]: Severe throttling is occurring.
     *                  - [PowerManager.THERMAL_STATUS_CRITICAL]: Critical throttling is occurring.
     *                  - [PowerManager.THERMAL_STATUS_EMERGENCY]: Emergency throttling is occurring.
     *                  - [PowerManager.THERMAL_STATUS_SHUTDOWN]: The device is shutting down due to thermal conditions.
     *                  - -1: Initial status couldn't be retrieved (e.g., PowerManager is null).
     *
     * @throws IllegalStateException If the device is running a version of Android lower than Q.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun Context.getThermalStatusFlow(): Flow<Int> = callbackFlow {
        val thermalStatusListener = PowerManager
            .OnThermalStatusChangedListener { status ->
                trySend(status).isSuccess
                Log.d("ThermalStatusFlow", "Thermal Status changed to $status")
            }

        powerManager.takeIf { it != null } ?: throw IllegalStateException("PowerManager is null")
        Log.d("getThermalStatusFlow", "PowerManager is not null")

        // Register the listener
        powerManager?.addThermalStatusListener(thermalStatusListener)

        // Unregister the listener when the flow is no longer collected
        awaitClose {
            Log.d("ThermalStatusFlow", "Unregistering thermal status listener")
            powerManager?.removeThermalStatusListener(thermalStatusListener)
        }
    }.onStart {
        //Get initial status and emit it.
        val status = powerManager?.currentThermalStatus ?: -1
        emit(status)
        Log.d("ThermalStatusFlow", "Initial Thermal Status: $status")
    }

    /**
     * A convenient inline property to retrieve the [PowerManager] system service.
     *
     * This property provides a shorthand way to access the [PowerManager] instance
     * associated with the current [Context]. The [PowerManager] allows you to
     * control the device's power state, such as managing wake locks, detecting
     * whether the screen is on, and checking power-saving modes.
     *
     * @see Context.getSystemService
     * @see PowerManager
     * @return The [PowerManager] instance, or null if the system service is not available.
     *
     * @throws ClassCastException if the system service is not of type [PowerManager] (highly unlikely).
     *
     * Example Usage:
     * ```kotlin
     * val isScreenOn = if(powerManager?.isInteractive == true){
     *     //Screen is on
     * }else {
     *      //Screen is off
     * }
     * ```
     */
    private inline val Context.powerManager
        @SuppressLint("InlinedApi")
        get() = getSystemService(Context.POWER_SERVICE) as? PowerManager

}
