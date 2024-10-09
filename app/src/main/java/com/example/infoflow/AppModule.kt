package com.example.infoflow

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import com.example.common.AppDispatchers
import com.example.data.repositories.FavoriteRepositoryImpl
import com.example.data.repositories.interfaces.NewsRepository
import com.example.data.repositories.NewsRepositoryImpl
import com.example.data.repositories.interfaces.FavoriteRepository
import com.example.database.NewsDatabase
import com.example.news.opennews_api.NewsApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Singleton
    @Binds
    fun bindRepositoryNews(impl: NewsRepositoryImpl): NewsRepository
    @Singleton
    @Binds
    fun bindRepositoryFavorites(impl: FavoriteRepositoryImpl): FavoriteRepository

    companion object {
        @Provides
        @Singleton
        fun provideNewsApi(): NewsApi {
            return NewsApi(
                baseUrl = BuildConfig.NEWS_API_BASE_URL,
                apiKey = BuildConfig.NEWS_API_KEY,
            )
        }

        @Provides
        @Singleton
        fun provideNewsDatabase(
            @ApplicationContext context: Context
        ): NewsDatabase {
            return NewsDatabase(context)
        }

        @Provides
        @Singleton
        fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

        @Provides
        @Singleton
        fun provideImageLoader(
            @ApplicationContext context: Context
        ): ImageLoader {
            return ImageLoader.Builder(context)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        }

    }

}