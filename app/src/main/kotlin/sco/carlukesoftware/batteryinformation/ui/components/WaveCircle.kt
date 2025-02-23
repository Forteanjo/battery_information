package sco.carlukesoftware.batteryinformation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.batteryinformation.ui.theme.Blue600
import kotlin.math.sin

/**
 * Creates an animated wave effect within a circular shape.
 *
 * This composable draws a circle and animates a wave within it, simulating a liquid level.
 * The wave's behavior, including its height, speed, and appearance, can be customized.
 *
 * @param modifier Modifier to be applied to the Box containing the wave circle.
 * @param waterLevel The water level within the circle, represented as a float between 0.0 (empty) and 1.0 (full).
 * @param waveColor The color of the wave.
 * @param circleColor The background color of the circle.
 * @param waveAmplitude The amplitude (height) of the wave, represented as a float between 0.0 and 1.0 relative to the circle height.
 * @param waveFrequency The frequency of the wave, determining the number of wave peaks and troughs. Higher values result in more waves.
 * @param waveSpeed The duration in milliseconds for one complete wave cycle. Lower values result in faster wave movement.
 */
@Composable
fun AnimatedWaveCircle(
    modifier: Modifier = Modifier,
    waterLevel: Float = 0.5f, // 0.0 (empty) to 1.0 (full)
    waveColor: Color = Blue600,
    circleColor: Color = Color.LightGray,
    waveAmplitude: Float = 0.4f, // Amplitude of the wave (0.0 to 1.0)
    waveFrequency: Float = 1.5f, // Frequency of the wave (higher = more waves)
    waveSpeed: Int = 4000 // Duration of one wave cycle in milliseconds
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveAnimation")
    val waveShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = waveSpeed,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "waveShift"
    )

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val circleRadius = canvasWidth.coerceAtMost(canvasHeight) / 2f
            val circleCenter = Offset(canvasWidth / 2f, canvasHeight / 2f)

            // Draw the circle
            drawCircle(
                color = circleColor,
                radius = circleRadius,
                center = circleCenter
            )

            // Calculate the water level position
            val waterLevelY = canvasHeight * (1f - waterLevel)

            // Create the wave path
            val wavePathFront = createWavePath(
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                waterLevelY = waterLevelY - (waveShift * 0.9f),
                waveShift = waveShift,
                waveAmplitude = waveAmplitude,
                waveFrequency = waveFrequency
            )
            val wavePathBack = createWavePath(
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                waterLevelY = waterLevelY * 1.1f,
                waveShift = 1.0f - waveShift, // Invert the wave shift for the back path
                waveAmplitude = waveAmplitude * 1.1f,
                waveFrequency = waveFrequency
            )

            // Clip the wave to the circle
            val circlePath = Path().apply {
                addOval(
                    Rect(
                        offset = Offset(
                            x = circleCenter.x - circleRadius,
                            y = circleCenter.y - circleRadius
                        ),
                        size = Size(
                            width = circleRadius * 2,
                            height = circleRadius * 2
                        )
                    )
                )
            }

            clipPath(circlePath) {
                // Draw the wave
                drawPath(
                    path = wavePathFront,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            waveColor.copy(alpha = 1.0f),
                            waveColor.copy(alpha = 0.6f),
                            waveColor.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )

                drawPath(
                    path = wavePathBack,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            waveColor.copy(alpha = 0.6f),
                            waveColor.copy(alpha = 0.4f),
                            waveColor.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
            }
        }
    }
}

private fun DrawScope.createWavePath(
    canvasWidth: Float,
    canvasHeight: Float,
    waterLevelY: Float,
    waveShift: Float,
    waveAmplitude: Float,
    waveFrequency: Float
): Path {
    val path = Path()

    val derivedHeight = (-0.8f * waveShift)

    // Start at the bottom-left
    path.moveTo(
        x = 0f, 
        y = canvasHeight
    )

    val waveHeight = canvasHeight * (waveAmplitude/10)
    val waveLength = canvasWidth / waveFrequency

    for (x in 0..canvasWidth.toInt()) {
        val y = waterLevelY + waveHeight *
                sin(2 * Math.PI * (x / waveLength + waveShift)).toFloat()
        path.lineTo(
            x = x.toFloat(),
            y = y
        )
    }

    with (path) {
        // Go to the bottom-right
        lineTo(
            x = canvasWidth,
            y = canvasHeight
        )

        // Close the path to fill the area below the wave
        close()
    }

    return path
}

@Preview(showBackground = true)
@Composable
fun AnimatedWaveCirclePreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AnimatedWaveCircle(
                modifier = Modifier
                    .size(200.dp),
                waterLevel = 0.7f
            )
        }
    }
}
