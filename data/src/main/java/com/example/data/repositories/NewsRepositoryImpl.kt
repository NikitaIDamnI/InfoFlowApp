package com.example.data.repositories

import android.app.Application
import android.util.Log
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import com.example.data.R.string.non_existent_http
import com.example.data.mappers.toArticle
import com.example.data.mappers.toArticleDbo
import com.example.data.mappers.toDto
import com.example.data.mappers.toUiArticle
import com.example.data.model.Article
import com.example.data.model.SortBy
import com.example.data.repositories.interfaces.NewsRepository
import com.example.news.api.NewsApi
import com.example.news.api.models.ArticleDTO
import com.example.news.api.models.Language
import com.example.news.api.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.collections.map

public class NewsRepositoryImpl @Inject constructor(
    private val application: Application,
    private val api: NewsApi,
) : NewsRepository {


    override fun getEverythingNews(): Flow<List<ArticleUI>> = flow {
        val articles = loadGetEverythingNewsFromApi().map { it.toUiArticle() }
        emit(articles)
    }

    override fun getTopHeadlinersNews(): Flow<List<ArticleUI>> = flow {
        val articles = loadTopHeadlines().map { it.toUiArticle() }
        emit(articles)
    }

    suspend fun searchNews(
        query: String?,
        category: CategoryNews
    ): List<ArticleUI> {
        val resultSearch =
            if (isSpecificCategory(category)) {
                loadGetEverythingNewsFromApi(
                    query = query,
                ).map { it.toUiArticle() }
            } else {
                loadTopHeadlines(
                    query = query,
                    category = category
                ).map { it.toUiArticle() }
            }

        return resultSearch
    }


    private fun filterContent(it: ArticleDTO): Boolean {
        return (it.url != application.getString(non_existent_http)) && (it.content != null)
    }

    private suspend fun loadGetEverythingNewsFromApi(
        query: String? = "All world news",
        from: String? = null,
        to: String? = null,
        sortBy: SortBy? = SortBy.POPULARITY,
        languages: List<Language> = listOf(Language.RU, Language.EN)
    ): List<Article> = api.everything(
        query = query,
        from = from,
        to = to,
        sortBy = sortBy?.toDto(),
        languages = languages,
    ).resultApi()

    private suspend fun loadTopHeadlines(
        query: String? = null,
        country: String? = "us",
        category: CategoryNews = CategoryNews.ENTERTAINMENT,
    ): List<Article> = api.topHeadlines(
        query = query,
        country = country,
        category = category.name,
        sources = null,
    ).resultApi()


    fun Result<ResponseDTO<ArticleDTO>>.resultApi(): List<Article> {
        when {
            this.isSuccess -> {
                val resultSuccess = this.getOrThrow().articles
                Log.d("NewsRepositoryImpl_Log", "resultApi : $resultSuccess ")
                return resultSuccess
                    .filter { filterContent(it) }
                    .map { it.toArticle() }
            }

            this.isFailure -> {
                error(this.exceptionOrNull() ?: "Unknown error try again later")
            }

            else -> error("Something went wrong")
        }
    }

    private fun isSpecificCategory(category: CategoryNews): Boolean {
        return category != CategoryNews.ALL &&
                category != CategoryNews.RECOMMENDATION &&
                category != CategoryNews.TOP_HEADLINES
    }

}
