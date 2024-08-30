package com.example.data

import com.example.data.model.Article
import com.example.database.NewsDatabase
import com.example.news.opennews_api.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

public class TestArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun testGetArticle(): Flow<List<Article>> = database.articlesDao.observeAll()
        .filter { it.isEmpty() }
        .map { it.toArticle() }

    suspend fun testLoadArticlesFromApi() {
        val articlesFromApi = api.everything()
        when {
            articlesFromApi.isSuccess -> {
                val resultSuccess = articlesFromApi.getOrThrow().articles.toArticleDbo()
                database.articlesDao.insert(resultSuccess)
            }

            articlesFromApi.isFailure -> {
                error(articlesFromApi.exceptionOrNull() ?: "Unknown error try again later")
            }

            else -> error("Something went wrong")
        }
    }

}