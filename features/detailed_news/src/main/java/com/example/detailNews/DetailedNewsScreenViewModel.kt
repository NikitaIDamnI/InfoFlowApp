package com.example.detailNews

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.mergeWith
import com.example.detailNews.DetailedNewsScreenState.StateHttpContent
import com.example.domain.useCase.ManageFavoritesUseCase
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
    private val manageFavorites: ManageFavoritesUseCase,
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
                isFavorite = manageFavorites.checkFavorite(
                    articleUI
                ),
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
                manageFavorites.addToFavorites(articleUI)
            } else {
                manageFavorites.deleteToFavorites(articleUI)
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
            } catch (e: kotlin.Exception) {
                Log.e("DetailedNewsScreenViewModel_Log", "Error loading content", e)
                loadHtmlContentEvent.emit(
                    state.value.copy(
                        httpContent = StateHttpContent.Error("Error loading content")
                    )
                )
            }
        }
    }
}
