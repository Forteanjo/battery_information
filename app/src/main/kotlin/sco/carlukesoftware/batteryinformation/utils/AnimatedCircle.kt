package sco.carlukesoftware.batteryinformation.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode.Companion.SrcIn
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp

val gradientColors = listOf(
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Magenta,
    Color.LightGray,
    Color.Red
)

fun Modifier.drawAnimationBorder(
    strokeWidth: Dp,
    shape: Shape,
    brush: (Size) -> Brush = {
        Brush.sweepGradient(
            colors = gradientColors,
        )
    },
    durationMillis: Int = 3500,
    enabled: Boolean = true
) = composed {
    if (!enabled) return@composed this

    composed {
        val infiniteTransition = rememberInfiniteTransition(
            label = "Rotation"
        )

        val angle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "Rotation"
        )

        Modifier
            .clip(shape)
            .drawWithCache {
                val strokeWidthPx = strokeWidth.toPx()
                val outline: Outline = shape
                    .createOutline(
                        size = size,
                        layoutDirection = layoutDirection,
                        density = this
                    )

                onDrawWithContent {
                    drawContent()
                    with(drawContext.canvas.nativeCanvas) {
                        val checkPoint = saveLayer(null,null)
                        drawOutline(
                            outline = outline,
                            color = Color.LightGray,
                            style = Stroke(
                                width = strokeWidthPx
                            )
                        )

                        // Source
                        rotate(angle) {
                            drawCircle(
                                brush = brush(size),
                                radius = size.width,
                                blendMode = SrcIn
                            )
                        }

                        restoreToCount(checkPoint)
                    }
                }
            }
    }
}
