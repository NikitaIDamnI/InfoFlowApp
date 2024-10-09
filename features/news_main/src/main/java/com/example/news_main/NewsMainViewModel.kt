package com.example.news_main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.common.models.ArticleUI
import com.example.data.useCase.GetArticleUseCase
import com.example.data.toUiArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    getArticleUseCase: GetArticleUseCase,
    val imageLoader: ImageLoader
) : ViewModel() {


    val state: StateFlow<NewsMainScreenState> = combine(
        getArticleUseCase.getTopHeadlinersFromApi(),
        getArticleUseCase.getEverythingFromApi()
    ) { topHeadlines, everything ->
        NewsMainScreenState(
            topHeadlines = topHeadlines.map { it.toUiArticle() },
            recommendations = everything.map { it.toUiArticle() },
            stateLoaded = NewsMainScreenState.TestStateLoaded.Success
        )
    }
        .catch {
            NewsMainScreenState(
                topHeadlines = emptyList(),
                recommendations = emptyList(),
                stateLoaded = NewsMainScreenState.TestStateLoaded.Error(it.message.toString())
            )
        }
        .mergeFavorites(getArticleUseCase.getFromFavorite())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = NewsMainScreenState(
                topHeadlines = emptyList(),
                recommendations = emptyList(),
                stateLoaded = NewsMainScreenState.TestStateLoaded.Initial
            )
        )


    init {
        viewModelScope.launch{
            state.collect{
                Log.d("NewsMainViewModel_Log", "${it.stateLoaded}")
            }
        }
    }

}


fun Flow<NewsMainScreenState>.mergeFavorites(favorites: Flow<List<ArticleUI>>): Flow<NewsMainScreenState> {
    return combine(this, favorites) { state, favoriteArticles ->
        state.copy(favorites = favoriteArticles)
    }
}





