package com.example.detail_news

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.mergeWith
import com.example.data.NewsRepositoryImpl
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val favoritesEvent: MutableSharedFlow<DetailedNewsScreenState> = MutableSharedFlow(1)
    private val loadHtmlContentEvent: MutableSharedFlow<DetailedNewsScreenState> = MutableSharedFlow(1)

    private val articleUI = Screen.DetailedNews.from(savedStateHandle).articleUI


    val state: StateFlow<DetailedNewsScreenState> = flow {
        emit(DetailedNewsScreenState(article = articleUI, isFavorite = repository.checkFavorite(articleUI)))
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
            initialValue = DetailedNewsScreenState(article = articleUI, isFavorite = false)
        )

    init{
        viewModelScope.launch{
            state.collect{
                Log.d("DetailedNewsScreenViewModel_Log", it.htmlContent.toString())

            }
        }
    }


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
            val htmlContent = try {
                withContext(Dispatchers.IO) {
                    val document = Jsoup.connect(url).get()
                    document.html()
                }
            } catch (e: Exception) {
                Log.e("HTMLContentError", "Error loading content", e)
                e.printStackTrace()
                null
            }

            loadHtmlContentEvent.emit(state.value.copy(htmlContent = htmlContent))
        }
    }

}