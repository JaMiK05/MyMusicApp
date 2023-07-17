package uz.gita.music_app_jamik.ui.componenta

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import io.github.ningyuv.circularseekbar.CircularSeekbarView
import uz.gita.music_app_jamik.R
import java.lang.Math.*

/**
 *   Created by Jamik on 7/13/2023 ot 6:43 PM
 **/

/**
dependency circular seekbar
implementation 'io.github.ningyuv:circular-seek-bar:0.0.3'
 **/
@Composable
fun CircularSeekBar(
    value: Int = 0,
    size: Dp = 200.dp,
    onChangeListener: ((Float) -> Unit)? = null,
    steps: Int = 15,
    lineWeight: Dp = 20.dp,
    dotRadius: Int = 25,
    activeColor: Color = Color(LocalContext.current.getColor(R.color.green)),
    inActiveColor: Color = Color.Gray,
    dotColor: Color = Color.Black,
) {
    Box(
        modifier = Modifier.size(size + 5.dp),
        contentAlignment = Alignment.Center
    ) {
        val pr = 0f
        CircularSeekbarView(
            value = pr,
            onChange = {},
            activeColor = activeColor,
            dotRadius = 0.dp,
            lineWeight = 2.dp,
            inactiveColor = activeColor
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 15.dp),
                text = "Volume",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Green
            )
        }
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {

            var progres by rememberSaveable {
                mutableStateOf(value.toFloat() / steps.toFloat())
            }

            CircularSeekbarView(value = progres,
                onChange = { v ->
                    onChangeListener?.invoke(v * steps)
                    progres = v
                },
                steps = steps,
                startAngle = -120f,
                fullAngle = 240f,
                lineWeight = lineWeight,
                dotRadius = dotRadius.dp,
                activeColor = activeColor,
                inactiveColor = inActiveColor,
                dotColor = dotColor,
                drawDot = { dotCenter, angle, color, radius ->
                    val starPath = Path()
                    val n = 8
                    starPath.moveTo(dotCenter.x + 0f, dotCenter.y - radius)
                    for (i in 0 until n) {
                        val rad1 = PI * 2 / (n * 2) * (i * 2 + 1) - PI / 2
                        val offset1 = Offset(
                            (dotCenter.x + cos(rad1) * radius * cos(PI * 2 / n) / cos(PI / n)).toFloat(),
                            (dotCenter.y + sin(rad1) * radius * cos(PI * 2 / n) / cos(PI / n)).toFloat()
                        )
                        starPath.lineTo(offset1.x, offset1.y)
                        val rad2 = PI * 2 / (n * 2) * (i * 2 + 2) - PI / 2
                        val offset2 = Offset(
                            (dotCenter.x + cos(rad2) * radius).toFloat(),
                            (dotCenter.y + sin(rad2) * radius).toFloat()
                        )
                        starPath.lineTo(offset2.x, offset2.y)
                    }
                    rotate(angle + 90f, dotCenter) {
                        drawPath(starPath, color)
                    }
                })

            Text(
                modifier = Modifier,
                text = (progres * steps).toInt().toString(),
                fontSize = (dotRadius * 3).sp,
                color = activeColor
            )
        }
    }
}

@Preview
@Composable
private fun Sekto() {
    MaterialTheme() {
        Surface() {
            CircularSeekBar(
                size = 250.dp,
                onChangeListener = {},
            )
        }
    }
}
