package com.example.detail_news

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.mergeWith
import com.example.data.NewsRepositoryImpl
import com.example.detail_news.DetailedNewsScreenState.StateHttpContent
import com.example.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class DetailedNewsScreenViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val favoritesEvent: MutableSharedFlow<DetailedNewsScreenState> = MutableSharedFlow(1)
    private val loadHtmlContentEvent: MutableSharedFlow<DetailedNewsScreenState> =
        MutableSharedFlow(1)

    private val articleUI = Screen.DetailedNews.from(savedStateHandle).articleUI


    val state: StateFlow<DetailedNewsScreenState> = flow {
        emit(
            DetailedNewsScreenState(
                article = articleUI,
                isFavorite = repository.checkFavorite(articleUI),
                httpContent = StateHttpContent.Loading,
            )
        )
    }
        .onStart {
            extractHtmlContent(
                url = articleUI.url,
            )
        }
        .mergeWith(loadHtmlContentEvent)
        .mergeWith(favoritesEvent)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DetailedNewsScreenState(
                article = articleUI,
                isFavorite = false,
                httpContent = StateHttpContent.Initial
            )
        )


    fun addToFavorites() {
        viewModelScope.launch {
            favoritesEvent.emit(state.value.copy(isFavorite = !state.value.isFavorite))
        }
    }

    fun updateFavoritesCache() {
        viewModelScope.launch {
            if (state.value.isFavorite) {
                repository.addToFavorites(articleUI)
            } else {
                repository.deleteToFavorites(articleUI)
            }
        }
    }


    fun extractHtmlContent(url: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val document = Jsoup.connect(url).get()
                    loadHtmlContentEvent.emit(
                        state.value.copy(
                            httpContent = StateHttpContent.Success(document.html())
                        )
                    )

                }
            } catch (e: Exception) {
                loadHtmlContentEvent.emit(
                    state.value.copy(
                        httpContent = StateHttpContent.Error("Error loading content")
                    )
                )
            }
        }
    }

}