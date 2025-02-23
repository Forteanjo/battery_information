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
