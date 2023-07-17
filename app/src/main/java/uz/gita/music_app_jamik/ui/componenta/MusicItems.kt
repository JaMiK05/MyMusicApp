package uz.gita.music_app_jamik.ui.componenta

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import uz.gita.music_app_jamik.data.model.MusicData
import uz.gita.music_app_jamik.R
import uz.gita.music_app_jamik.util.MyEventBus
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.background
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke


/**
 *   Created by Jamik on 7/15/2023 ot 6:58 PM
 **/


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayMusicItem(
    musicData: MusicData,
    index: Int,
    onLongClick: ((MusicData) -> Unit)? = null,
) {

    val cn = LocalContext.current
    val color = if (index % 2 == 0) Color.Black else Color(cn.getColor(R.color.black_och))
    var textColor by remember { mutableStateOf(Color.White) }
    if (MyEventBus.currentMusicData.collectAsState().value != null) {
        val music = MyEventBus.currentMusicData.collectAsState().value!!
        val bool =
            (musicData.title == music.title) && (musicData.artist == music.artist) && (musicData.data == music.data)
        if (bool) textColor = Color(cn.getColor(R.color.green)) else textColor = Color.White
        Log.d("YYY", "$bool")
    } else {
        textColor = Color.White
        Log.d("YYY", "$")
    }

    val milliseconds = musicData.duration

    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = (milliseconds / 1000 / 60) % 60
    val seconds = (milliseconds / 1000) % 60

    val time = if (hours == 0L) "%02d:%02d".format(minutes, seconds)
    else "%02d:%02d:%02d".format(hours, minutes, seconds)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .height(50.dp)
            .padding(vertical = 2.dp)
            .padding(start = 5.dp)
            .combinedClickable(onLongClick = { onLongClick?.invoke(musicData) }, onClick = {}),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(25.dp),
            text = "${index+1}",
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 5.dp)
        ) {
            Text(
                modifier = Modifier,
                text = musicData.title ?: "---------",
                color = textColor,
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = musicData.artist ?: "Unknown",
                color = textColor,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Text(
            modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .align(Alignment.CenterVertically),
            text = time,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

    }

}

@Preview
@Composable
private fun MusicItemPrev() {
    val data = MusicData(
        id = 1,
        artist = "somonjon abdumurodov ganiyevich qolbola wogirdi",
        title = "Azeri bass Muzzik | bilmem hangi ruzgar",
        data = "",
        duration = 20_010_000L,
    )
    MaterialTheme() {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.background(Color.Black)) {
                PlayMusicItem(data, 0)
                PlayMusicItem(data, 1)
                PlayMusicItem(data, 2)
                PlayMusicItem(data, 3)
            }
        }
    }
}