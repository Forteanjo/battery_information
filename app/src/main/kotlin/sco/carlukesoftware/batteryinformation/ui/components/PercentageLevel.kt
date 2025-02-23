package sco.carlukesoftware.batteryinformation.ui.components

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.batteryinformation.ui.theme.Rose100

@Composable
fun PercentageLevel(
    @IntRange(from = 0, to = 100)
        percent: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val percentage = percent / 100f

        AnimatedWaveCircle(
            modifier = Modifier
                .fillMaxSize(),
            waterLevel = percentage,
            waveColor = Color.Red,
            circleColor = Rose100
        )

        EmbossedText(
            text = "$percent%",
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),
            style = MaterialTheme.typography.headlineLarge
        )

    }
}
