package com.example.search.search_content_feature

import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews

data class SearchScreenState(
    val query: String? = null,
    val searchResult: List<ArticleUI> = emptyList(),
    val category: CategoryNews = CategoryNews.ALL,
    val stateLoaded: TestStateLoaded = TestStateLoaded.Initial

) {
    sealed interface TestStateLoaded {
        data object Initial : TestStateLoaded
        data object Success : TestStateLoaded
        data class Error(val message: String) : TestStateLoaded
        data object Loading : TestStateLoaded
    }

}


