package sco.carlukesoftware.batteryinformation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.batteryinformation.R
import sco.carlukesoftware.batteryinformation.ui.theme.BatteryInformationTheme

@Composable
fun BatteryInfoComponent(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    header: String,
    headerTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    value: String,
    valueTextStyle: TextStyle = MaterialTheme.typography.headlineLarge
) {
    Row(
        modifier = Modifier
            .padding(all = 4.dp)
            .then(modifier),
        horizontalArrangement =  Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .height(72.dp)
                .width(72.dp),
            colorFilter = ColorFilter
                .lighting(
                    multiply = Color.Black,
                    add = Color.Black
                )
        )

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(6.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = header,
                modifier = Modifier
                    .padding(top = 6.dp),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                style = headerTextStyle
            )

            Text(
                text = value,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = valueTextStyle
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BatteryInfoComponentPreview() {
    BatteryInformationTheme {
        BatteryInfoComponent(
            modifier = Modifier
                .height(96.dp),
            imageRes = R.drawable.bolt,
            header = "Voltage",
            value = "${5}v")
    }

}
