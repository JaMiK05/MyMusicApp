package uz.gita.music_app_jamik.domain

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import uz.gita.music_app_jamik.data.model.MusicData
import uz.gita.music_app_jamik.util.MyEventBus
import uz.gita.music_app_jamik.util.getMusicCursor
import uz.gita.music_app_jamik.util.getMusicDataByPosition
import javax.inject.Inject

/**
 *   Created by Jamik on 7/17/2023 ot 8:07 PM
 **/
class AppRepositoryImpl @Inject constructor() : AppRepository {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun getAllMusic(context: Context): Flow<List<MusicData>> =
        callbackFlow {
            val arraylist = ArrayList<MusicData>()
            context.getMusicCursor().onEach { cursor ->
                for (i in 0 until cursor.count) {
                    val musData = cursor.getMusicDataByPosition(i)
                    if (musData.duration != 0L && !(MyEventBus.musicList.value.contains(musData))) {
                        arraylist.add(musData)
                        MyEventBus.musicList.value.add(musData)
                    }
                    trySend(arraylist)
                }
            }.launchIn(scope)
            MyEventBus.listSize = MyEventBus.musicList.value.size
            awaitClose()
        }

}