package uz.gita.music_app_jamik.presentation.play

import android.database.Cursor
import kotlinx.coroutines.flow.StateFlow

/**
 *   Created by Jamik on 7/15/2023 ot 6:52 PM
 **/
interface PlayContract {

    data class UiState(
        val cursor: Cursor? = null,
    )

    sealed interface Intent {
        object IsRepated : Intent
        object IsRandom : Intent
        object NavigateMusicList : Intent
    }

    interface ViewModel {

        val uiState: StateFlow<UiState>

        fun onEvenDispatcher(intent: Intent)

    }

}