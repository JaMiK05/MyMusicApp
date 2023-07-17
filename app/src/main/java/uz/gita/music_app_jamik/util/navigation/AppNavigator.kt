package uz.gita.music_app_jamik.util.navigation

import cafe.adriel.voyager.androidx.AndroidScreen

/**
 *   Created by Jamik on 6/15/2023 ot 3:37 PM
 **/

typealias AppScreen = AndroidScreen

interface AppNavigator {

    suspend fun stackLog()
    suspend fun back()
    suspend fun backUntilRoot()
    suspend fun backAll()
    suspend fun navigateTo(screen: AppScreen)
    suspend fun replace(screen: AppScreen)
    suspend fun replaceAll(screen: AppScreen)

}