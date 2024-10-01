package com.example.common

import kotlinx.serialization.Serializable

@Serializable
data class ArticleUI(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val url: String,
    val content: String,
    val author: String,
    val publishedAt: String
)


