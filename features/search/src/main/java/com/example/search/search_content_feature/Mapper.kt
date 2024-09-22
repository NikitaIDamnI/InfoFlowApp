package com.example.search.search_content_feature

import com.example.data.model.Article

internal fun Article.toUiArticle(): com.example.common.ArticleUI {
    return com.example.common.ArticleUI(
        id = source?.id ?: "",
        title = title ?: "",
        description = description ?: "",
        imageUrl = urlToImage ?: "",
        url = url ?: "",
        content = content ?: "",
        author = author ?: "",
        publishedAt = publishedAt

    )
}