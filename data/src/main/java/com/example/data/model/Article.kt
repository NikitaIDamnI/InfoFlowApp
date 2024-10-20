package com.example.data.model

import java.util.Date

data class Article(
    val cacheId: Long = ID_NONE,
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: Date?,
    val content: String?
) {
    companion object {
        /**
         * Обозначает отсутствие ID
         */
        const val ID_NONE: Long = 0L
    }
}

data class Source(
    val id: String,
    val name: String
)
