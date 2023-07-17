package uz.gita.music_app_jamik.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import uz.gita.music_app_jamik.data.model.CommandEnum
import uz.gita.music_app_jamik.service.MusicService

fun Any.startMusicService(context: Context, commandEnum: CommandEnum) {
    val intent = Intent(context, MusicService::class.java)
    intent.putExtra("COMMAND", commandEnum)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else context.startService(intent)
}