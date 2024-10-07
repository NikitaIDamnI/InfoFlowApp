package com.example.data

import android.util.Log
import com.example.common.ArticleUI
import com.example.common.CategoryNews
import com.example.data.model.Article
import com.example.data.model.SortBy
import com.example.database.NewsDatabase
import com.example.news.opennews_api.NewsApi
import com.example.news.opennews_api.models.ArticleDTO
import com.example.news.opennews_api.models.CategoryNewsDTO
import com.example.news.opennews_api.models.Language
import com.example.news.opennews_api.models.SortByDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

public class NewsRepositoryImpl @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi,
) : NewsRepository {


    override fun getEverythingNews(): Flow<List<Article>> = flow {
        val dbArticles = loadGetEverythingNewsFromApi()
        emit(dbArticles)
    }

    override fun getTopHeadlinersNews(): Flow<List<Article>> = flow {
        val dbArticles = loadGetEverythingNewsFromApi()
        emit(dbArticles)
    }

    override suspend fun loadTopHeadlines(
        query: String?,
        country: String?,
        category: CategoryNews,
    ): List<Article> {

        val articlesTopHeadlines = api.topHeadlines(
            query = null,
            country = "us",
            category = CategoryNewsDTO.GENERAL.name,
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

     fun getFavorites(): Flow<List<ArticleUI>>{
        return database.articlesDao.observeAll().map { it.map { it.toArticleUI() } }
    }


    private fun filterContent(it: ArticleDTO) =
        it.url != "https://removed.com" && it.content != null && it.urlToImage != null

    override suspend fun loadGetEverythingNewsFromApi(
        query: String?,
        from: String?,
        to: String?,
        sortBy: SortBy?,
    ): List<Article> {
        val articlesEverything = api.everything(
            query = query ?: "All world news",
            sortBy = sortBy?.toDto() ?: SortByDto.POPULARITY,
            languages = listOf(Language.RU, Language.EN),
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
        Log.d("RepositoryArticles", "isFavorite: $isFavorite")
        return isFavorite == 1
    }


}