package com.example.news_main.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TestArticlesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestNewsMainViewModel(
    private val repository: TestArticlesRepository
) : ViewModel() {

    private val loadNewArticlesEvent = MutableSharedFlow<TestState.TestStateLoaded>(1)
    val state = repository.testGetArticle()
        .filter { it.isNotEmpty() }
        .map { TestState(
            articles = it,
            stateLoaded = TestState.TestStateLoaded.Initial,
        ) }
        .onStart {loadNewArticles()}
        .mergeStateLoaded(loadNewArticlesEvent)
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = TestState())



    fun loadNewArticles() {
        viewModelScope.launch {
            loadNewArticlesEvent.emit(TestState.TestStateLoaded.Loading)
            try {
                repository.testLoadArticlesFromApi()
                loadNewArticlesEvent.emit(TestState.TestStateLoaded.Success)
            } catch (e: RuntimeException) {
                loadNewArticlesEvent.emit(TestState.TestStateLoaded.Error(e.message?:"Error"))
            }
        }

    }
}




public data class ArticleUI(
    val id: Long,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val url: String?
)