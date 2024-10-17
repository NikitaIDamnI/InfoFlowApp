package com.example.data.repositories

import com.example.common.models.ArticleUI
import com.example.data.mappers.toArticleDbo
import com.example.data.mappers.toArticleUI
import com.example.data.repositories.interfaces.FavoriteRepository
import com.example.database.NewsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val database: NewsDatabase

) : FavoriteRepository {

    override suspend fun addToFavorites(articleUI: ArticleUI) {
        val articleDBO = articleUI.toArticleDbo()
        database.articlesDao.insert(articleDBO)
    }

    override suspend fun deleteToFavorites(articleUI: ArticleUI) {
        database.articlesDao.remove(articleUI.url)
    }

    override fun getFavorites(): Flow<List<ArticleUI>> {
        return database.articlesDao.observeAll().map { it.map { it.toArticleUI() } }
    }


    override suspend fun checkFavorite(article: ArticleUI): Boolean {
        val isFavorite = database.articlesDao.checkFavorite(article.url)
        return isFavorite == 1
    }

}
