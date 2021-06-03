package com.example.mikebamb.di

import android.app.Application
import androidx.room.Room
import com.example.mikebamb.data.local.EquipmentDatabase

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

   /* @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(APIrequests.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAPIrequests(retrofit: Retrofit): APIrequests =
        retrofit.create(APIrequests::class.java)*/


}