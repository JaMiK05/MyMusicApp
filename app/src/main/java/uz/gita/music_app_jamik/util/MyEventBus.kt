package uz.gita.music_app_jamik.util

import android.database.Cursor
import kotlinx.coroutines.flow.MutableStateFlow
import uz.gita.music_app_jamik.data.model.MusicData
import uz.gita.music_app_jamik.data.model.SpeedEnum

/**
 *   Created by Jamik on 7/15/2023 ot 3:23 PM
 **/
object MyEventBus {

    var currentMusicPos = MutableStateFlow(0)

    var playMusicPos: Int = -1

    var duration = MutableStateFlow(0L)

    var currentTime = MutableStateFlow(0L)

    var listSize: Int = 0

    var cursor: Cursor? = null

    var musicList = MutableStateFlow<ArrayList<MusicData>>(arrayListOf())

    val speed = MutableStateFlow(SpeedEnum.NORMAL)

    var isPlaying = MutableStateFlow(false)

    var message = MutableStateFlow("")

    val currentMusicData = MutableStateFlow<MusicData?>(null)

    val isRepeated = MutableStateFlow(false)

    val isRandom = MutableStateFlow(false)

}