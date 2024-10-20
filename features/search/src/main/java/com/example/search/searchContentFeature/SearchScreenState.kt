package com.example.search.searchContentFeature

import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews

data class SearchScreenState(
    val query: String? = null,
    val searchResult: List<ArticleUI> = emptyList(),
    val category: CategoryNews = CategoryNews.ALL,
    val stateLoaded: StateLoaded = StateLoaded.Initial
) {
    sealed interface StateLoaded {
        data object Initial : StateLoaded
        data object Success : StateLoaded
        data class Error(val message: String) : StateLoaded
        data object Loading : StateLoaded
    }
}
