package sco.carlukesoftware.batteryinformation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.batteryinformation.ui.screens.BatteryInfoScreen
import sco.carlukesoftware.batteryinformation.ui.theme.BatteryInformationTheme
import sco.carlukesoftware.batteryinformation.utils.drawAnimationBorder

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryInformationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    val screenWidth = LocalConfiguration.current.screenWidthDp

                    BatteryInfoScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
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
