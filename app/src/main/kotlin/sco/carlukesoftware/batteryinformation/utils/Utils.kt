package sco.carlukesoftware.batteryinformation.utils

import android.graphics.PointF
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import kotlin.math.pow

fun lerpF(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop

fun parabolaInterpolation(fraction: Float): Float {
    return ((-40) * (fraction - 0.5).pow(2) + 11).toFloat()
}

fun Int.toBoolean(): Boolean = this != 0

@Stable
fun TextUnit.toPx(density: Density): Float = with(density) {
    this@toPx.roundToPx().toFloat()
}
