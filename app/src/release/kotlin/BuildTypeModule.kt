package com.example.infoflow

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BuildTypeModule {
    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}
