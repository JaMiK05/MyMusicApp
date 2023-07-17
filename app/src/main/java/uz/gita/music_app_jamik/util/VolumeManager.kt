package uz.gita.music_app_jamik.util

import android.content.Context
import android.media.AudioManager
import android.util.Log

/**
 *   Created by Jamik on 7/14/2023 ot 3:40 PM
 **/
fun setVolume(volume: Float, context: Context) {
    val audio: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
    Log.d("TTT", currentVolume.toString())
    if (currentVolume < volume) {
        val count = (volume - currentVolume).toInt()
        Log.d("TTT", "upper")
        for (i in 0 until count) {
            audio.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
        }
    } else {
        val count = (currentVolume - volume).toInt()
        Log.d("TTT", "lower")
        for (i in 0 until count) {
            audio.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
        }
    }
}