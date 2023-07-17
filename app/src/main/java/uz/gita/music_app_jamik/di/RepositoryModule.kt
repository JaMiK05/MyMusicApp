package uz.gita.music_app_jamik.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.music_app_jamik.domain.*
import javax.inject.Singleton

/**
 *   Created by Jamik on 7/17/2023 ot 8:17 PM
 **/
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAppRepository(impl: AppRepositoryImpl): AppRepository

}