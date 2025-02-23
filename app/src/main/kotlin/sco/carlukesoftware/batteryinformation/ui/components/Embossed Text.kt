package sco.carlukesoftware.batteryinformation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Creates a text composable with an embossed (raised) effect.
 *
 * This composable renders a given text string with a simulated 3D embossed appearance.
 * It achieves this by overlaying three text layers: a dark shadow, a light highlight,
 * and the main text. The shadow and highlight are offset from the main text to create
 * the illusion of depth.
 *
 * @param text The string to be displayed with the embossed effect.
 * @param modifier Modifiers to be applied to the layout.
 * @param style The style of the text, such as font family, size, and weight.
 * @param lightColor The color of the light highlight, simulating a light source. Defaults to white.
 * @param darkColor The color of the dark shadow, simulating a shadowed area. Defaults to gray.
 * @param offset The offset of the shadow from the main text. This determines the depth and direction of the emboss effect.
 *               Positive x moves the shadow to the right, positive y moves it down. The highlight will be offset in the opposite direction.
 *               Defaults to IntOffset(x = 4, y = 4).
 *
 * Example Usage:
 * ```
 * EmbossedText(
 *     text = "Embossed Text",
 *     style = TextStyle(
 *         fontSize = 24.sp,
 *         fontWeight = FontWeight.Bold
 *     ),
 *     lightColor = Color.LightGray,
 *     darkColor = Color.DarkGray,
 *     offset = IntOffset(4, 4)
 * )
 * ```
 */
@Composable
fun EmbossedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    lightColor: Color = Color.White,
    darkColor: Color = Color.Gray,
    offset: IntOffset = IntOffset(
        x = 4,
        y = 4
    )
) {
    Box(
        modifier = modifier
    ) {
        // Dark shadow
        Text(
            text = text,
            style = style
                .copy(
                    color = lightColor
                ),
            modifier = Modifier
                .offset {
                    offset
                }
        )

        // Light highlight
        Text(
            text = text,
            style = style
                .copy(
                    color = darkColor
                ),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = -offset.x,
                        y = -offset.y
                    )
                }
        )

        // Main text
        Text(
            text = text,
            style = style,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmbossedTextComposePreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            EmbossedText(
                text = "Embossed Text",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
    }
}
