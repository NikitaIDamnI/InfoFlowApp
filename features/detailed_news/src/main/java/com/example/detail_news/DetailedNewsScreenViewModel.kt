package com.example.detail_news

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.common.ArticleUI
import com.example.common.Screen
import com.example.common.mergeWith
import com.example.data.model.Article
import com.example.data.test.NewsRepositoryImpl
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class DetailedNewsScreenViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val favoritesEvent:MutableSharedFlow<Boolean> = MutableSharedFlow(1)

    private val articleJson: String? = savedStateHandle[Screen.KEY_ARTICLE]
    private val articleUI: ArticleUI = articleJson.let { Gson().fromJson(it, ArticleUI::class.java) }


    val state: StateFlow<DetailedNewsScreenState> = flow<DetailedNewsScreenState>{
        val isFavorite =  repository.checkFavorite(articleUI)
        emit(DetailedNewsScreenState(article = articleUI, isFavorite = isFavorite))
    }
        .mergeFavoriteEvent(favoritesEvent)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DetailedNewsScreenState(article = articleUI, isFavorite = false)
        )

    fun addToFavorites(){
        viewModelScope.launch {
            favoritesEvent.emit(!state.value.isFavorite)
            repository.addToFavorites(articleUI)
        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<DetailedNewsScreenState>.mergeFavoriteEvent(favoritesEvent: Flow<Boolean>): Flow<DetailedNewsScreenState> {
    return flatMapMerge { state ->
        favoritesEvent.map { isFavorite ->
            state.copy(isFavorite = isFavorite)
        }
    }
}