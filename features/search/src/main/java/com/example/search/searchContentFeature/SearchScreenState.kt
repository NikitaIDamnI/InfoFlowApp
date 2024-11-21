@file:Suppress("SpacingBetweenDeclarationsWithAnnotations")

package com.example.search.searchContentFeature

import androidx.compose.runtime.Stable
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews

@Stable
data class SearchScreenState(
    val query: String? = null,
    val searchResult: List<ArticleUI> = emptyList(),
    val currentCategory: CategoryNews = CategoryNews.ALL,
    val stateLoaded: StateLoaded = StateLoaded.Initial
) {
    sealed interface StateLoaded {
        @Stable
        data object Initial : StateLoaded
        @Stable
        data object Success : StateLoaded
        @Stable
        data class Error(val message: String) : StateLoaded
        @Stable
        data object Loading : StateLoaded
    }
}
