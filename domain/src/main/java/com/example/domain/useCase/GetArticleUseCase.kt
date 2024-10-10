package com.example.domain.useCase

import com.example.common.models.ArticleUI
import com.example.data.repositories.NewsRepositoryImpl
import com.example.data.model.Article
import com.example.data.repositories.FavoriteRepositoryImpl
import kotlinx.coroutines.flow.Flow

class GetArticleUseCase @javax.inject.Inject constructor(
    private val repository: NewsRepositoryImpl,
    private val favoriteRepository: FavoriteRepositoryImpl
) {
    fun getFromFavorite() = favoriteRepository.getFavorites()

    fun getTopHeadlinersFromApi(): Flow<List<ArticleUI>> {
        return repository.getTopHeadlinersNews()
    }

    fun getEverythingFromApi(): Flow<List<ArticleUI>> {
        return repository.getEverythingNews()

    }



}