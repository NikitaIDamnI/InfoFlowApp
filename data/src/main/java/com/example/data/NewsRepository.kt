package com.example.data

import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import com.example.data.model.Article
import com.example.data.model.SortBy
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getEverythingNews(): Flow<List<Article>>


    fun getTopHeadlinersNews(): Flow<List<Article>>


    suspend fun addToFavorites(articleUI: ArticleUI)
}