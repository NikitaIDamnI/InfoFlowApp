package com.example.news_main

import com.example.common.ArticleUI


data class TestNewsMainScreenState(
    val topHeadlines: List<ArticleUI> = emptyList(),
    val recommendations: List<ArticleUI> = emptyList(),
    val stateLoaded: TestStateLoaded = TestStateLoaded.Null

) {
    sealed interface TestStateLoaded {
        data object Null : TestStateLoaded
        data object Initial : TestStateLoaded
        data object Success : TestStateLoaded
        data class Error(val message: String) : TestStateLoaded
        data object Loading : TestStateLoaded
    }
}

