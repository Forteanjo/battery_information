package sco.carlukesoftware.batteryinformation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WaveLoaderDefaults(
    val amplitudeRatio: Float = DEFAULT_AMPLITUDE_RATIO,
    val waterLevelRatio: Float = DEFAULT_WATER_LEVEL_RATIO,
    val waveLengthRatio: Float = DEFAULT_WAVE_LENGTH_RATIO,
    val waveShiftRatio: Float = DEFAULT_WAVE_SHIFT_RATIO,
    val waveShiftDuration: Int = DEFAULT_WAVE_SHIFT_DURATION,
    val waveColor: Color = DEFAULT_WAVE_COLOR,
    val hasBorder: Boolean = true,
    val borderWidth: Dp = DEFAULT_BORDER_WIDTH,
    val canvasWidth: Dp = DEFAULT_CANVAS_WIDTH,
    val canvasHeight: Dp = DEFAULT_CANVAS_HEIGHT
) {

    companion object {
        const val DEFAULT_AMPLITUDE_RATIO = 0.05f
        const val DEFAULT_WATER_LEVEL_RATIO = 0.5f
        const val DEFAULT_WAVE_LENGTH_RATIO = 1.0f
        const val DEFAULT_WAVE_SHIFT_RATIO = 0.0f
        const val DEFAULT_WAVE_SHIFT_DURATION = 1000

        val DEFAULT_WAVE_COLOR = Color.Black

        val DEFAULT_BORDER_WIDTH = 10.dp
        val DEFAULT_CANVAS_WIDTH = 440.dp
        val DEFAULT_CANVAS_HEIGHT = 440.dp
    }

}
