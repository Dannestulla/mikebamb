package com.gohan.mikebamb.main_app.di

import android.app.Application
import androidx.room.Room
import com.gohan.mikebamb.main_app.data.local.EquipmentDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): EquipmentDatabase =
        Room.databaseBuilder(app, EquipmentDatabase::class.java, "equipment")
            .build()
}