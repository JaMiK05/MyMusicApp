package uz.gita.music_app_jamik.util.navigation

import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.Flow

/**
 *   Created by Jamik on 6/15/2023 ot 3:37 PM
 **/

typealias NavigationArgs = Navigator.() -> Unit


interface NavigationHandler {
    val navigationStack: Flow<NavigationArgs>

}