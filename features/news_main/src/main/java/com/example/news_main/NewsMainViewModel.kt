package com.example.news_main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.common.ArticleUI
import com.example.common.mergeWith
import com.example.data.NewsRepositoryImpl
import com.example.data.toUiArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl,
    val imageLoader: ImageLoader
) : ViewModel() {

    private val loadNewArticlesEvent = MutableSharedFlow<NewsMainScreenState>(1)

    val state: StateFlow<NewsMainScreenState> = flowOf<NewsMainScreenState>()
        .map {
            NewsMainScreenState()
        }
        .onStart {
            loadNews()
        }
        .mergeWith(loadNewArticlesEvent)
        .mergeFavorites(repository.getFavorites())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = NewsMainScreenState(
                topHeadlines = emptyList(),
                recommendations = emptyList(),
                stateLoaded = NewsMainScreenState.TestStateLoaded.Initial
            )
        )

    private fun loadNews() {
        Log.d("TestNewsMainViewModel_Log", "Start loadNews")


        val loadRecommendations = viewModelScope.async {
            loadNewArticlesEvent.emit(state.value.copy(stateLoaded = NewsMainScreenState.TestStateLoaded.Loading))
            repository.loadGetEverythingNewsFromApi().map { it.toUiArticle() }
        }


        val loadTopHeadlines = viewModelScope.async {
            repository.loadTopHeadlines().map { it.toUiArticle() }
        }

        viewModelScope.launch {
            try {
                Log.d("TestNewsMainViewModel_Log", "Start try")

                val recommendations = loadRecommendations.await()
                val topHeadlines = loadTopHeadlines.await()

                Log.d("TestNewsMainViewModel_Log", "Start ${recommendations.get(0)}")
                Log.d("TestNewsMainViewModel_Log", "Start ${topHeadlines.get(0)}")


                loadNewArticlesEvent.emit(
                    state.value.copy(
                        topHeadlines = topHeadlines,
                        recommendations = recommendations,
                        stateLoaded = NewsMainScreenState.TestStateLoaded.Success
                    )
                )

            } catch (e: Exception) {
                Log.d("TestNewsMainViewModel_Log", "Start catch")

                loadNewArticlesEvent.emit(
                    state.value.copy(
                        stateLoaded = NewsMainScreenState.TestStateLoaded.Error(
                            e.message ?: ""
                        )
                    )
                )
            }
        }
    }

}


fun Flow<NewsMainScreenState>.mergeFavorites(favorites: Flow<List<ArticleUI>>): Flow<NewsMainScreenState> {
    return combine(this, favorites) { state, favoriteArticles ->
        state.copy(favorites = favoriteArticles)
    }
}





