package com.example.data.repositories.interfaces

import com.example.common.models.ArticleUI
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addToFavorites(articleUI: ArticleUI)

    suspend fun deleteToFavorites(articleUI: ArticleUI)

    fun getFavorites(): Flow<List<ArticleUI>>

    suspend fun checkFavorite(article: ArticleUI): Boolean
}
