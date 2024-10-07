package com.example.detail_news

import com.example.common.ArticleUI


data class DetailedNewsScreenState(
    val article: ArticleUI,
    val isFavorite: Boolean,
    val htmlContent: String? = null
) {
    sealed interface TestStateLoaded {
        data object Null : TestStateLoaded
        data object Initial : TestStateLoaded
        data object Success : TestStateLoaded
        data class Error(val message: String) : TestStateLoaded
        data object Loading : TestStateLoaded
    }
}


