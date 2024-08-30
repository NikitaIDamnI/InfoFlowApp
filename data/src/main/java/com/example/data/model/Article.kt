package com.example.data.model

import java.util.Date

public data class Article(
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
    public companion object {
        /**
         * Обозначает отсутствие ID
         */
        public const val ID_NONE: Long = 0L
    }
}

public data class Source(
    val id: String,
    val name: String
)