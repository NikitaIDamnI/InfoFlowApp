package com.example.data.useCase

import com.example.common.models.ArticleUI
import com.example.data.repositories.NewsRepositoryImpl
import com.example.data.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val repository: NewsRepositoryImpl
) {
    fun getFromFavorite() = repository.getFavorites()

    fun getTopHeadlinersFromApi(): Flow<List<Article>> {
        return repository.getTopHeadlinersNews()
    }

    fun getEverythingFromApi(): Flow<List<Article>> {
        return repository.getEverythingNews()

    }

    suspend fun checkFavoriteArticle(articleUI: ArticleUI) = repository.checkFavorite(articleUI)


}