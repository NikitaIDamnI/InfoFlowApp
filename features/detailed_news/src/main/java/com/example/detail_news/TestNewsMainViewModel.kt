package com.example.detail_news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.mergeWith
import com.example.data.model.Article
import com.example.data.test.NewsRepositoryImpl
import com.example.data.toUiArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestNewsMainViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl
) : ViewModel() {

    private val loadNewArticlesEvent = MutableSharedFlow<TestNewsMainScreenState>(1)

    val state = repository.getEverythingNews()
        .onStart { loadNews() }
        .mergeStateLoaded(loadNewArticlesEvent)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TestNewsMainScreenState(
                topHeadlines = emptyList(),
                recommendations = emptyList(),
                stateLoaded = TestNewsMainScreenState.TestStateLoaded.Initial
            )
        )

    private fun loadNews() {

        val loadRecommendations = viewModelScope.async {
            loadNewArticlesEvent.emit(state.value.copy(stateLoaded = TestNewsMainScreenState.TestStateLoaded.Loading))
            repository.loadGetEverythingNewsFromApi().map { it.toUiArticle() }
        }

        val loadTopHeadlines = viewModelScope.async {
            repository.loadTopHeadlines().map { it.toUiArticle() }
        }

        viewModelScope.launch {
            try {
                val recommendations = loadRecommendations.await()
                val topHeadlines = loadTopHeadlines.await()

                loadNewArticlesEvent.emit(
                    state.value.copy(
                        topHeadlines = topHeadlines,
                        recommendations = recommendations,
                        stateLoaded = TestNewsMainScreenState.TestStateLoaded.Success
                    )
                )

            } catch (e: Exception) {
                loadNewArticlesEvent.emit(
                    state.value.copy(
                        stateLoaded = TestNewsMainScreenState.TestStateLoaded.Error(
                            e.message ?: ""
                        )
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<Article>>.mergeStateLoaded(stateLoadedFlow: Flow<TestNewsMainScreenState>): Flow<TestNewsMainScreenState> {
    return flatMapMerge { _ ->
        stateLoadedFlow.map { newStateLoaded ->
            TestNewsMainScreenState(
                topHeadlines = newStateLoaded.topHeadlines,
                stateLoaded = newStateLoaded.stateLoaded,
                recommendations = newStateLoaded.recommendations
            )
        }
    }
}





