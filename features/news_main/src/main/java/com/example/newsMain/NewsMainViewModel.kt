package com.example.newsMain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.common.models.ArticleUI
import com.example.domain.useCase.GetArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
            topHeadlines = topHeadlines.map { it },
            recommendations = everything.map { it },
            stateLoaded = NewsMainScreenState.StateLoaded.Success
        )
    }
        .catch { e ->
            emit(
                NewsMainScreenState(
                    topHeadlines = emptyList(),
                    recommendations = emptyList(),
                    stateLoaded = NewsMainScreenState.StateLoaded.Error("Download error")
                )
            )
        }
        .combineFavorites(getArticleUseCase.getFromFavorite())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = NewsMainScreenState(
                topHeadlines = emptyList(),
                recommendations = emptyList(),
                stateLoaded = NewsMainScreenState.StateLoaded.Loading
            )
        )
}

fun Flow<NewsMainScreenState>.combineFavorites(favorites: Flow<List<ArticleUI>>): Flow<NewsMainScreenState> {
    return combine(this, favorites) { state, favoriteArticles ->
        state.copy(favorites = favoriteArticles)
    }
}
