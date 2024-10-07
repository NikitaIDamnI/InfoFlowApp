package com.example.data

import com.example.common.ArticleUI
import com.example.common.CategoryNews
import com.example.data.model.Article
import com.example.data.model.SortBy
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getEverythingNews(): Flow<List<Article>>

    suspend fun loadGetEverythingNewsFromApi(
        query: String? = null,
        from: String? = null,
        to: String? = null,
        sortBy: SortBy? = null,
    ): List<Article>

    fun getTopHeadlinersNews(): Flow<List<Article>>

    suspend fun loadTopHeadlines(
        query: String? = null,
        country: String? = null,
        category: CategoryNews = CategoryNews.TECHNOLOGY
    ): List<Article>

    suspend fun addToFavorites(articleUI: ArticleUI)
}