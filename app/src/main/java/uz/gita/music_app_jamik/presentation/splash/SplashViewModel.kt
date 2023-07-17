package uz.gita.music_app_jamik.presentation.splash

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import uz.gita.music_app_jamik.domain.AppRepository
import uz.gita.music_app_jamik.util.MyEventBus
import uz.gita.music_app_jamik.util.getMusicCursor
import uz.gita.music_app_jamik.util.getMusicDataByPosition
import javax.inject.Inject

/**
 *   Created by Jamik on 7/15/2023 ot 1:29 PM
 **/
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: AppRepository,
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


                repository.getAllMusic(intent.context)
                    .onEach { }.launchIn(viewModelScope)

                if (MyEventBus.listSize > 0) {
                    MyEventBus.currentMusicData.value = MyEventBus.musicList.value[0]
                    MyEventBus.musicPos.value = 0
                }

                viewModelScope.launch {
                    delay(3000)
                    direction.navigate()
                }
            }
        }
    }
}