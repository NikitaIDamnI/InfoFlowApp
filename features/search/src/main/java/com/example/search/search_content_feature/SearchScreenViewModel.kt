package com.example.search.search_content_feature

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.common.models.CategoryNews
import com.example.data.repositories.NewsRepositoryImpl
import com.example.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl,
    savedStateHandle: SavedStateHandle,
    val imageLoader: ImageLoader
) : ViewModel() {

    private val categoryFromArg = Screen.Search.from(savedStateHandle).category
    private val contentFromArg = Screen.Search.from(savedStateHandle).content


    private val _state = MutableStateFlow(
        SearchScreenState(category = categoryFromArg, searchResult = contentFromArg)
    )
    val state = _state.asStateFlow()

    private var searchJob: Job? = null


    fun onQueryChange(query: String) {
        _state.value = _state.value.copy(query = query)
    }

    fun onCategoryChange(category: CategoryNews) {
        _state.value = _state.value.copy(category = category)
    }

    fun onSearchNews(query: String) {
        Log.d("SearchScreenViewModel_Log", "onSearchNews ($query)")

        if (query != "") {
            loadNews(
                query = query,
                category = _state.value.category
            )
        }
    }

    private fun loadNews(
        query: String?,
        category: CategoryNews
    ) {

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                _state.value =
                    _state.value.copy(stateLoaded = SearchScreenState.TestStateLoaded.Loading)

                val resultSearch =
                    if (category != CategoryNews.ALL && category != CategoryNews.RECOMMENDATION && category != CategoryNews.TOP_HEADLINES) {
                        repository.loadGetEverythingNewsFromApi(
                            query = query,
                        ).map { it.toUiArticle() }
                    } else {
                        repository.loadTopHeadlines(
                            query = query,
                            category = category
                        ).map { it.toUiArticle() }
                    }
                _state.value = _state.value.copy(
                    searchResult = resultSearch,
                    stateLoaded = SearchScreenState.TestStateLoaded.Success
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    stateLoaded = SearchScreenState.TestStateLoaded.Error(
                        message = e.message.toString()
                    )
                )
            }

        }
    }


}










