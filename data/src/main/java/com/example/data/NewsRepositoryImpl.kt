package com.example.data

import android.app.Application
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import com.example.data.model.Article
import com.example.data.model.SortBy
import com.example.database.NewsDatabase
import com.example.news.opennews_api.NewsApi
import com.example.news.opennews_api.models.ArticleDTO
import com.example.news.opennews_api.models.CategoryNewsDTO
import com.example.news.opennews_api.models.Language
import com.example.news.opennews_api.models.SortByDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class NewsRepositoryImpl @Inject constructor(
    private val application: Application,
    private val database: NewsDatabase,
    private val api: NewsApi,
) : NewsRepository {


    override fun getEverythingNews(): Flow<List<Article>> = flow {
        val articles = loadGetEverythingNewsFromApi()
        emit(articles)
    }

    override fun getTopHeadlinersNews(): Flow<List<Article>> = flow {
        val articles = loadTopHeadlines()
        emit(articles)
    }

    suspend fun loadTopHeadlines(
        query: String? = null,
        country: String? = "us",
        category: CategoryNews = CategoryNews.GENERAL,
    ): List<Article> {


        val articlesTopHeadlines = api.topHeadlines(
            query = null,
            country = "us",
            category = category.name,
            sources = null,
        )
        when {
            articlesTopHeadlines.isSuccess -> {
                val resultSuccess = articlesTopHeadlines.getOrThrow().articles
                    .filter { filterContent(it) }
                    .toArticleDbo()

                return resultSuccess.map { it.toArticle() }
            }

            articlesTopHeadlines.isFailure -> {
                error(articlesTopHeadlines.exceptionOrNull() ?: "Unknown error try again later")
            }

            else -> error("Something went wrong")
        }

    }

    override suspend fun addToFavorites(articleUI: ArticleUI) {
        val articleDBO = articleUI.toArticleDbo()
        database.articlesDao.insert(articleDBO)
    }

    suspend fun deleteToFavorites(articleUI: ArticleUI) {
        database.articlesDao.remove(articleUI.url)
    }

    fun getFavorites(): Flow<List<ArticleUI>> {
        return database.articlesDao.observeAll().map { it.map { it.toArticleUI() } }
    }


    private fun filterContent(it: ArticleDTO): Boolean {
        return it.url != application.getString(R.string.non_existent_http)
                && it.content != null && it.urlToImage != null
    }

    suspend fun loadGetEverythingNewsFromApi(
        query: String? = "All world news",
        from: String? = null,
        to: String? = null,
        sortBy: SortBy? = SortBy.POPULARITY,
        languages: List<Language> = listOf(Language.RU, Language.EN)
    ): List<Article> {
        val articlesEverything = api.everything(
            query = query,
            from = from,
            to = to,
            sortBy = sortBy?.toDto(),
            languages = languages,
        )

        when {
            articlesEverything.isSuccess -> {
                val resultSuccess = articlesEverything.getOrThrow().articles
                    .filter { filterContent(it) }
                    .toArticleDbo()

                return resultSuccess.map { it.toArticle() }
            }

            articlesEverything.isFailure -> {
                error(articlesEverything.exceptionOrNull() ?: "Unknown error try again later")
            }

            else -> error("Something went wrong")
        }
    }

    suspend fun checkFavorite(article: ArticleUI): Boolean {
        val isFavorite = database.articlesDao.checkFavorite(article.url)
        return isFavorite == 1
    }


}