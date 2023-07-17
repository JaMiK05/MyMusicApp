package uz.gita.music_app_jamik.di

import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.music_app_jamik.presentation.play.*
import uz.gita.music_app_jamik.presentation.splash.*
import javax.inject.Singleton

/**
 *   Created by Jamik on 7/15/2023 ot 1:36 PM
 **/

@Module
@InstallIn(SingletonComponent::class)
interface DirectionModule {

    @Binds
    @Singleton
    fun splashDirectionBinds(impl: SplashDirectionImpl): SplashDirection

    @Binds
    @Singleton
    fun playScreenDirectionBinds(impl: PlayDirectionImpl): PlayDirection

}