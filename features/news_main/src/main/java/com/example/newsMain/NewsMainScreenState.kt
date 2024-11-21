@file:Suppress("SpacingBetweenDeclarationsWithAnnotations")

package com.example.newsMain

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.common.models.ArticleUI

@Stable
data class NewsMainScreenState(
    val topHeadlines: List<ArticleUI> = emptyList(),
    val recommendations: List<ArticleUI> = emptyList(),
    val favorites: List<ArticleUI> = emptyList(),
    val stateLoaded: StateLoaded
) {
    sealed interface StateLoaded {
        @Immutable
        data object Initial : StateLoaded
        @Stable
        data object Success : StateLoaded
        @Stable
        data class Error(val message: String) : StateLoaded
    }
}
