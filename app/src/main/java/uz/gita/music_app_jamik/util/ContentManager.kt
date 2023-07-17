package uz.gita.music_app_jamik.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.flow.*
import uz.gita.music_app_jamik.data.model.MusicData

private val projection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.ALBUM_ID
)

fun Context.getMusicCursor(): Flow<Cursor> = flow {
    val cursor: Cursor = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        MediaStore.Audio.Media.IS_MUSIC + "!=0",
        null,
        null
    ) ?: return@flow
    emit(cursor)
}

fun Cursor.getMusicDataByPosition(pos: Int): MusicData {
    this.moveToPosition(pos)

    var albumArt: Bitmap?
    try {
        val mmr = MediaMetadataRetriever()

        mmr.setDataSource(this.getString(3))
        albumArt = getBitmap(mmr.embeddedPicture)
        mmr.release()
    } catch (e: Exception) {
        albumArt = null
    }

    return MusicData(
        id = this.getInt(0),
        artist = this.getString(1),
        title = this.getString(2),
        data = this.getString(3),
        duration = this.getLong(4),
        albumArt = albumArt
    )
}

private fun getBitmap(byteArray: ByteArray?): Bitmap? {
    if (byteArray != null) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    return null
}