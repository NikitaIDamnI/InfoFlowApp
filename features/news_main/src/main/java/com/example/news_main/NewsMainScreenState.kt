package com.example.news_main

import com.example.common.models.ArticleUI


data class NewsMainScreenState(
    val topHeadlines: List<ArticleUI> = emptyList(),
    val recommendations: List<ArticleUI> = emptyList(),
    val favorites: List<ArticleUI> = emptyList(),
    val stateLoaded: TestStateLoaded

) {
    sealed interface TestStateLoaded {
        data object Initial : TestStateLoaded
        data object Success : TestStateLoaded
        data class Error(val message: String) : TestStateLoaded
    }
}


