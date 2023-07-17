package uz.gita.music_app_jamik.data.model

import android.graphics.Bitmap

data class MusicData(
    val id: Int,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Long,
    val albumArt: Bitmap? = null,
    val storagePosition: Int = 0
) 