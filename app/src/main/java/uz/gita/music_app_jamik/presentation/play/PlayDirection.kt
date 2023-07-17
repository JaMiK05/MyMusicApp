package uz.gita.music_app_jamik.presentation.play

import uz.gita.music_app_jamik.util.navigation.AppNavigator
import javax.inject.Inject

/**
 *   Created by Jamik on 7/16/2023 ot 1:22 PM
 **/
interface PlayDirection {
    suspend fun navigateToScreen()
}

class PlayDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
) : PlayDirection {

    override suspend fun navigateToScreen() {
        navigator
    }

}