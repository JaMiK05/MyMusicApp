package uz.gita.music_app_jamik.data.model

import android.graphics.Bitmap

data class MusicData(
    val id: Int,
    val artist: String?,
    val title: String?,
    val data: String?,
    val duration: Long,
    val albumArt: Bitmap? = null,
    val storagePosition: Int = 0,
) {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is MusicData) return false
        return ((id == other.id) && (title == other.title) && (artist == other.artist))
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (artist?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }

}