package com.example.data

import com.example.data.model.Article
import com.example.data.model.Source
import com.example.database.models.ArticleDBO
import com.example.database.models.Source as SourceDBO
import com.example.news.opennews_api.models.ArticleDTO
import com.example.news.opennews_api.models.SourceDTO

internal fun List<ArticleDBO>.toArticle(): List<Article> = this.map { it.toArticle() }
internal fun List<ArticleDTO>.toArticle(): List<Article> = this.map { it.toArticle() }
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