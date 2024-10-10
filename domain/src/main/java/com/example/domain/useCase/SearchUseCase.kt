package com.example.domain.useCase

import com.example.common.models.CategoryNews
import com.example.data.repositories.NewsRepositoryImpl
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: NewsRepositoryImpl
) {
    suspend operator fun invoke (query: String?, category: CategoryNews) =
        repository.searchNews(
            query = query,
            category = category
        )

}