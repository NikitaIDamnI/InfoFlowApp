package com.example.data.mappers

import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import com.example.data.model.Article
import com.example.data.model.SortBy
import com.example.database.models.ArticleDBO
import com.example.news.api.models.ArticleDTO
import com.example.news.api.models.CategoryNewsDTO
import com.example.news.api.models.SortByDto
import java.util.Date
import com.example.database.models.Source as SourceDBO

internal fun ArticleDTO.toArticle(): Article {
    return Article(
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

fun SortBy.toDto(): SortByDto {
    return when (this) {
        SortBy.RELEVANCY -> SortByDto.RELEVANCY
        SortBy.POPULARITY -> SortByDto.POPULARITY
        SortBy.PUBLISHED_AT -> SortByDto.PUBLISHED_AT
    }
}

fun ArticleUI.toArticleDbo(): ArticleDBO {
    return ArticleDBO(
        source = SourceDBO(id = id, name = id),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = imageUrl,
        publishedAt = publishedAt,
        content = content
    )
}

fun Article.toUiArticle(): ArticleUI {
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

fun ArticleDBO.toArticleUI(): ArticleUI {
    return ArticleUI(
        id = id.toString(),
        title = title ?: "",
        description = description ?: "",
        imageUrl = urlToImage ?: "",
        url = url ?: "",
        content = content ?: "",
        author = author ?: "",
        publishedAt = publishedAt ?: Date()

    )
}

fun CategoryNews.toDto(): CategoryNewsDTO? {
    return when (this) {
        CategoryNews.SPORTS -> CategoryNewsDTO.SPORTS
        CategoryNews.HEALTH -> CategoryNewsDTO.HEALTH
        CategoryNews.GENERAL -> CategoryNewsDTO.GENERAL
        CategoryNews.SCIENCE -> CategoryNewsDTO.SCIENCE
        CategoryNews.BUSINESS -> CategoryNewsDTO.BUSINESS
        CategoryNews.TECHNOLOGY -> CategoryNewsDTO.TECHNOLOGY
        CategoryNews.ENTERTAINMENT -> CategoryNewsDTO.ENTERTAINMENT
        else -> {
            null
        }
    }
}
