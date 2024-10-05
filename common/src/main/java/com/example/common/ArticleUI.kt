package com.example.common

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ArticleUI(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val url: String,
    val content: String,
    val author: String,
    @Serializable(with = DateTimeUTCSerializer::class)
    val publishedAt: Date
)


