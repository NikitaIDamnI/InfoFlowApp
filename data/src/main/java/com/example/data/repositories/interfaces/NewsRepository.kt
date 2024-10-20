package com.example.data.repositories.interfaces

import com.example.common.models.ArticleUI
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getEverythingNews(): Flow<List<ArticleUI>>
    fun getTopHeadlinersNews(): Flow<List<ArticleUI>>
}
