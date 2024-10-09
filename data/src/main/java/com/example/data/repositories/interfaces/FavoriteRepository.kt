package com.example.data.repositories.interfaces

import com.example.common.models.ArticleUI
import com.example.data.model.Article
import com.example.data.toArticleDbo
import com.example.data.toArticleUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FavoriteRepository {


    suspend fun addToFavorites(articleUI: ArticleUI)

    suspend fun deleteToFavorites(articleUI: ArticleUI)

    fun getFavorites(): Flow<List<ArticleUI>>

    suspend fun checkFavorite(article: ArticleUI): Boolean

}