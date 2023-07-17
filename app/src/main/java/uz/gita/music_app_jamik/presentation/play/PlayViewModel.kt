package uz.gita.music_app_jamik.presentation.play

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uz.gita.music_app_jamik.util.MyEventBus
import javax.inject.Inject

/**
 *   Created by Jamik on 7/16/2023 ot 1:22 PM
 **/
@HiltViewModel
class PlayViewModel @Inject constructor(
    private val direction: PlayDirection,
) : ViewModel(), PlayContract.ViewModel {
    override val uiState = MutableStateFlow(PlayContract.UiState())

    override fun onEvenDispatcher(intent: PlayContract.Intent) {
        when (intent) {
            is PlayContract.Intent.IsRandom -> {
                MyEventBus.isRandom.value = !MyEventBus.isRandom.value
            }

            is PlayContract.Intent.IsRepated -> {
                MyEventBus.isRepeated.value = !MyEventBus.isRepeated.value
            }

            is PlayContract.Intent.NavigateMusicList -> {

            }
        }
    }

}