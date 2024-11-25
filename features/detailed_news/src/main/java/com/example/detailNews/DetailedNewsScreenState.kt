package com.example.detailNews

data class DetailedNewsScreenState(
    val article: com.example.common.models.ArticleUI,
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
