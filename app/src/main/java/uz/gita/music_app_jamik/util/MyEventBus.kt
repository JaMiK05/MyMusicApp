package uz.gita.music_app_jamik.util

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow
import uz.gita.music_app_jamik.data.model.MusicData
import uz.gita.music_app_jamik.data.model.SpeedEnum

/**
 *   Created by Jamik on 7/15/2023 ot 3:23 PM
 **/
object MyEventBus {

    var musicPos: Int = -1

    var playMusicPos: Int = -1

    var musicTime = MutableStateFlow(0)

    var currentTime = MutableStateFlow(0)

    var listSize: Int = 0

    var cursor: Cursor? = null

    val speed = MutableStateFlow(SpeedEnum.Ortacha)

    var isPlaying = MutableStateFlow(false)

    var message = MutableStateFlow("")

    val currentMusicData = MutableStateFlow<MusicData?>(null)

    val isRepeated = MutableStateFlow(false)

    val isRandom = MutableStateFlow(false)

}