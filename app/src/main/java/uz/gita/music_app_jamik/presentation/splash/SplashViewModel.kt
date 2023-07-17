package uz.gita.music_app_jamik.presentation.splash

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import uz.gita.music_app_jamik.util.MyEventBus
import uz.gita.music_app_jamik.util.getMusicCursor
import uz.gita.music_app_jamik.util.getMusicDataByPosition
import javax.inject.Inject

/**
 *   Created by Jamik on 7/15/2023 ot 1:29 PM
 **/
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val direction: SplashDirection,
) : ViewModel(), SplashContract.ViewModel {
    override val uiState = MutableStateFlow(SplashContract.UiState(false))

    init {
        viewModelScope.launch {
            uiState.update { it.copy(request = true) }
            delay(7000)
        }
    }

    override fun onEventDispatcher(intent: SplashContract.Intent) {
        when (intent) {
            is SplashContract.Intent.NextScreen -> {
                intent.context.getMusicCursor().onEach { cur -> MyEventBus.cursor = cur }
                    .launchIn(viewModelScope)
                MyEventBus.listSize =
                    if (MyEventBus.cursor != null) MyEventBus.cursor!!.count else 0
                if (MyEventBus.listSize > 0) {
                    MyEventBus.currentMusicData.value =
                        MyEventBus.cursor!!.getMusicDataByPosition(0)
                    MyEventBus.musicPos = 0
                }

                viewModelScope.launch {
                    delay(1500)
                    direction.navigate()
                }
            }
        }
    }
}