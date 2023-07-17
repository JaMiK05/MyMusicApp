package uz.gita.music_app_jamik.di

import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.music_app_jamik.util.navigation.*
import javax.inject.Singleton

/**
 *   Created by Jamik on 6/14/2023 ot 5:20 PM
 **/

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @[Binds Singleton]
    fun bindAppNavigator(impl: NavigationDispatcher): AppNavigator

    @[Binds Singleton]
    fun bindNavigationHandler(impl: NavigationDispatcher): NavigationHandler

}