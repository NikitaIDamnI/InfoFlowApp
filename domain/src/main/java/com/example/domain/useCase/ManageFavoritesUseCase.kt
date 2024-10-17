package com.example.domain.useCase

import com.example.common.models.ArticleUI
import com.example.data.repositories.interfaces.FavoriteRepository
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
