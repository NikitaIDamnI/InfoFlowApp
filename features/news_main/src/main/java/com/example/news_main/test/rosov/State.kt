package com.example.news_main.test.rosov


import kotlinx.collections.immutable.ImmutableList

public sealed class State(public open val articles: ImmutableList<com.example.common.ArticleUI>?) {

    public data object None : State(articles = null)

    public class Loading(articles: ImmutableList<com.example.common.ArticleUI>? = null) : State(articles)

    public class Error(articles: ImmutableList<com.example.common.ArticleUI>? = null) : State(articles)

    public class Success(override val articles: ImmutableList<com.example.common.ArticleUI>) : State(articles)
}