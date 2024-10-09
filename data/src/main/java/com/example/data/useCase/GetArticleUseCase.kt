package com.example.data.useCase

import com.example.common.models.ArticleUI
import com.example.data.NewsRepositoryImpl
import com.example.data.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val repository: NewsRepositoryImpl
) {
    fun getFromFavorite() = repository.getFavorites()

     fun getFromApi(categoryApi: CategoryApi): Flow<List<Article>> {
        return when (categoryApi) {
            CategoryApi.TOP_HEADLINES -> repository.getTopHeadlinersNews()
            CategoryApi.EVERYTHING -> repository.getEverythingNews()
        }
    }

    suspend fun checkFavoriteArticle(articleUI: ArticleUI) = repository.checkFavorite(articleUI)

    enum class CategoryApi {
        TOP_HEADLINES, EVERYTHING
    }
}