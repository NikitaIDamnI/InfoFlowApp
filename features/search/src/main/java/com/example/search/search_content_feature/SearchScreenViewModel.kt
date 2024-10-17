package com.example.search.search_content_feature

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.common.models.CategoryNews
import com.example.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: com.example.domain.useCase.SearchUseCase,
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

    fun onSearchNews(query: String?) {
        if (!query.isNullOrBlank()) {
            loadNews(
                query = query,
                category = _state.value.category
            )
        }
    }

    private fun loadNews(
        query: String,
        category: CategoryNews
    ) {

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                _state.value =
                    _state.value.copy(stateLoaded = SearchScreenState.StateLoaded.Loading)

                val resultSearch = searchUseCase(
                    query = query,
                    category =category
                )

                _state.value = _state.value.copy(
                    searchResult = resultSearch,
                    stateLoaded = SearchScreenState.StateLoaded.Success
                )
            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    stateLoaded = SearchScreenState.StateLoaded.Error(
                        message = "Nothing found"
                    )
                )
            }

        }
    }


}










