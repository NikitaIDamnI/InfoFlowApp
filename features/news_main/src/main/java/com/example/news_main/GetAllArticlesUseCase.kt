package com.example.news_main

import com.example.data.ArticlesRepository
import com.example.data.RequestResult
import com.example.data.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {
    operator fun invoke(query: String): Flow<RequestResult<List<ArticleUI>>> {
        return repository.getAll(query).map { requestResult ->
            requestResult.map { articles -> articles.map { it.toUiArticle() } }
        }
    }
}