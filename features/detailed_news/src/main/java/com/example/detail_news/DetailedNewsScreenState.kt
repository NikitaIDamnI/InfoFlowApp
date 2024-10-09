package com.example.detail_news

import com.example.common.models.ArticleUI


data class DetailedNewsScreenState(
    val article: ArticleUI,
    val isFavorite: Boolean,
    val httpContent: StateHttpContent
) {
    sealed interface StateHttpContent {
        data object Initial : StateHttpContent
        data class Success(val htmlContent: String) : StateHttpContent
        data class Error(val message: String) : StateHttpContent
        data object Loading : StateHttpContent
    }
}


