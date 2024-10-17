package com.example.news.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SortByDto {
    @SerialName("relevancy")
    RELEVANCY,

    @SerialName("popularity")
    POPULARITY,

    @SerialName("publishedAt")
    PUBLISHED_AT
}
