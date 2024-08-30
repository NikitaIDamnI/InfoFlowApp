package com.example.news_main.test

import com.example.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class TestState(
    val articles: List<Article> = emptyList(),
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

fun Flow<TestState>.mergeStateLoaded(stateLoadedFlow: Flow<TestState.TestStateLoaded>): Flow<TestState> {
    return combine(this, stateLoadedFlow) { currentState, newStateLoaded ->
        currentState.copy(stateLoaded = newStateLoaded)
    }
}
