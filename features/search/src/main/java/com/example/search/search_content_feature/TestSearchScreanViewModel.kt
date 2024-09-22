package com.example.search.search_content_feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.CategoryNews
import com.example.data.test.NewsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl,
//    private val content: List<ArticleUI> = listOf(),
//    private val category: CategoryNews
) : ViewModel() {


    private val _state = MutableStateFlow(SearchScreenState(searchResult = listOf()))
    val state = _state.asStateFlow()


    private var searchJob: Job? = null


    init {
        //_state.value = _state.value.copy(searchResult = content, category = category)
        viewModelScope.launch {
            state.collect {
                Log.d("SearchScreenViewModel_Log", "${it.stateLoaded}")
                Log.d("SearchScreenViewModel_Log", "${it.searchResult}")
            }
        }
    }

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










