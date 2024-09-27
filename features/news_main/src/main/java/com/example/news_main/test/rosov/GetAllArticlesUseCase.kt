package com.example.news_main.test.rosov

import com.example.data.ArticlesRepository
import com.example.data.RequestResult
import com.example.data.map
import com.example.data.toUiArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {
    operator fun invoke(query: String): Flow<RequestResult<List<com.example.common.ArticleUI>>> {
        return repository.getAll(query).map { requestResult ->
            requestResult.map { articles -> articles.map { it.toUiArticle() } }
        }
    }
}