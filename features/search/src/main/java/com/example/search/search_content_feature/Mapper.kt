package com.example.search.search_content_feature

import com.example.common.models.ArticleUI
import com.example.data.model.Article
import java.util.Date

internal fun Article.toUiArticle(): ArticleUI {
    return ArticleUI(
        id = source?.id ?: "",
        title = title ?: "",
        description = description ?: "",
        imageUrl = urlToImage ?: "",
        url = url ?: "",
        content = content ?: "",
        author = author ?: "",
        publishedAt = publishedAt ?: Date()

    )
}