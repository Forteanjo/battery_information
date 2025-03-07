package sco.carlukesoftware.batteryinformation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import sco.carlukesoftware.batteryinformation.ui.screens.BatteryInfoScreen
import sco.carlukesoftware.batteryinformation.ui.theme.BatteryInformationTheme
import sco.carlukesoftware.batteryinformation.utils.drawAnimationBorder

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts
            .RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    println("Wake lock permission granted")
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied.
                    println("Wake lock permission denied")
                }
            }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val darkTheme = isSystemInDarkTheme()
            var isDarkTheme by remember {
                mutableStateOf(darkTheme)
            }

            RequestWakeLockPermission()
            RequestBatteryOptimizationsPermission()

            BatteryInformationTheme(isDarkTheme) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    val screenWidth = LocalConfiguration.current
                        .screenWidthDp

                    BatteryInfoScreen(
                        modifier = Modifier
                            .padding(innerPadding),
                        onDarkModeChanged = {
                            isDarkTheme = it

                        }
                    )
                }
            }
        }
    }

    @Composable
    fun RequestWakeLockPermission() {
        println("Wake Lock permission")
        val context = LocalContext.current
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WAKE_LOCK
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                println("Wake Lock permission already granted")
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.WAKE_LOCK
                )
            }
        }
    }

    @Composable
    fun RequestBatteryOptimizationsPermission() {
        println("Battery Optimizations")
        val context = LocalContext.current
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                println("Battery Optimizations permission already granted")
            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                )
            }
        }
    }

}


@Composable
fun DisplayCircle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .padding(16.dp)
                .drawAnimationBorder(
                    strokeWidth = 18.dp,
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
            Text(
                text = "Button"
            )
        }
    }
}
