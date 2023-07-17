package uz.gita.music_app_jamik.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import uz.gita.music_app_jamik.data.model.MusicData

/**
 *   Created by Jamik on 7/17/2023 ot 8:02 PM
 **/

interface AppRepository {
    fun getAllMusic(context: Context): Flow<List<MusicData>>
}