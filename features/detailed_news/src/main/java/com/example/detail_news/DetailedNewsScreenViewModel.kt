package com.example.detail_news

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import coil.ImageLoader
import com.example.common.ArticleUI
import com.example.data.test.NewsRepositoryImpl
import com.example.navigation.Screen
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject
import kotlin.coroutines.CoroutineContext.Element

@HiltViewModel
class DetailedNewsScreenViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl,
    private val savedStateHandle: SavedStateHandle,
    val imageLoader: ImageLoader
) : ViewModel() {

    private val favoritesEvent: MutableSharedFlow<Boolean> = MutableSharedFlow(1)

    private val articleUI = Screen.DetailedNews.from(savedStateHandle).articleUI


    val state: StateFlow<DetailedNewsScreenState> = combine(
        flowOf(
            DetailedNewsScreenState(
                article = articleUI,
                isFavorite = false
            )
        ), favoritesEvent
    ) { initialState, isFavorite ->
        initialState.copy(isFavorite = isFavorite)
    }
        .onStart {
            favoritesEvent.emit(repository.checkFavorite(articleUI))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DetailedNewsScreenState(article = articleUI, isFavorite = false)
        )

    init {
        viewModelScope.launch {
            favoritesEvent.collect {
                Log.d("DetailedNewsScreenViewModel_Log", "Избранное изменено: $it")
            }
        }
    }

    fun addToFavorites() {
        viewModelScope.launch {
            favoritesEvent.emit(!state.value.isFavorite)
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

//    suspend fun extractHtmlContent(url: String, partialContent: String): String? {
//        return withContext(Dispatchers.IO) {
//            try {
//                // Загружаем HTML-документ
//                val document = Jsoup.connect(url).get()
//
//                // Ищем элемент, который содержит часть контента
//                val element: Element? = document.body().getElementsContainingOwnText(partialContent).firstOrNull()
//
//                // Если элемент найден, ищем его родительский элемент с нужным классом
//                element?.parents()?.firstOrNull { it.hasClass("article-body-class") }?.html()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            }
//        }
//    }
}