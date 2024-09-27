package com.example.detail_news


data class TestNewsMainScreenState(
    val topHeadlines: List<com.example.common.ArticleUI>,
    val recommendations: List<com.example.common.ArticleUI>,
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


