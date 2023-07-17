package uz.gita.music_app_jamik.presentation.splash

import uz.gita.music_app_jamik.presentation.play.PlayScreen
import uz.gita.music_app_jamik.util.navigation.AppNavigator
import javax.inject.Inject

/**
 *   Created by Jamik on 7/15/2023 ot 1:32 PM
 **/
interface SplashDirection {
    suspend fun navigate()
}

class SplashDirectionImpl @Inject constructor(
    private val navigator: AppNavigator,
) : SplashDirection {
    override suspend fun navigate() {
        navigator.replace(PlayScreen())
    }

}