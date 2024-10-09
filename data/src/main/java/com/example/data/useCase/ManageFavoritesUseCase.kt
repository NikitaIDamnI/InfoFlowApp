package com.example.data.useCase

import com.example.common.models.ArticleUI
import com.example.data.repositories.NewsRepositoryImpl
import com.example.data.model.Article
import com.example.data.repositories.FavoriteRepositoryImpl
import com.example.data.repositories.interfaces.FavoriteRepository
import com.example.data.toArticleDbo
import com.example.data.toArticleUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ManageFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    suspend fun deleteToFavorites(articleUI: ArticleUI) {
        repository.deleteToFavorites(articleUI)
    }

    suspend fun checkFavorite(article: ArticleUI): Boolean {
        return repository.checkFavorite(article)
    }

    suspend fun addToFavorites(articleUI: ArticleUI) {
        repository.addToFavorites(articleUI)
    }

}