package uz.gita.music_app_jamik.presentation.splash

import android.content.Context
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

/**
 *   Created by Jamik on 7/15/2023 ot 1:25 PM
 **/
interface SplashContract {

    data class UiState(
        val request: Boolean = true,
    )

    sealed interface Intent {
        class NextScreen(val context: Context) : Intent
    }

    interface ViewModel {
        val uiState: StateFlow<UiState>

        fun onEventDispatcher(intent: Intent)
    }

}