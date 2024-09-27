package com.example.news_main.test.rosov

import com.example.data.RequestResult
import com.example.data.model.Article
import kotlinx.collections.immutable.toImmutableList

internal fun RequestResult<List<com.example.common.ArticleUI>>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(data?.toImmutableList())
        is RequestResult.InProgress -> State.Loading(data?.toImmutableList())
        is RequestResult.Success -> State.Success(data.toImmutableList())
    }
}



