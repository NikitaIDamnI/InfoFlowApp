package com.example.data

import com.example.common.models.ArticleUI
import com.example.data.model.Article
import com.example.data.model.Source
import com.example.data.model.SortBy
import com.example.database.models.ArticleDBO
import com.example.database.models.Source as SourceDBO
import com.example.news.opennews_api.models.ArticleDTO
import com.example.news.opennews_api.models.SortByDto
import com.example.news.opennews_api.models.SourceDTO
import java.util.Date

internal fun List<ArticleDBO>.toArticle(): List<Article> = this.map { it.toArticle() }

//internal fun List<ArticleDTO>.toArticle(): List<Article> = this.map { it.toArticle() }
internal fun List<ArticleDTO>.toArticleDbo(): List<ArticleDBO> = this.map { it.toArticleDbo() }


internal fun ArticleDBO.toArticle(): Article {
    return Article(
        cacheId = id,
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

internal fun SourceDBO.toSource(): Source {
    return Source(id = id, name = name)
}

internal fun SourceDTO.toSource(): Source {
    return Source(id = id ?: name, name = name)
}

internal fun SourceDTO.toSourceDbo(): SourceDBO {
    return SourceDBO(id = id ?: name, name = name)
}

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

internal fun ArticleDTO.toArticleDbo(): ArticleDBO {
    return ArticleDBO(
        source = source?.toSourceDbo(),
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

internal fun ArticleUI.toArticleDbo(): ArticleDBO {
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