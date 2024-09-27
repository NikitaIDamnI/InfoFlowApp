package com.example.news.opennews_api.models

import kotlinx.serialization.SerialName

enum class CategoryNewsDTO {
    @SerialName("business")
    BUSINESS,
    @SerialName("entertainment")
    ENTERTAINMENT,
    @SerialName("general")
    GENERAL,
    @SerialName("health")
    HEALTH,
    @SerialName("science")
    SCIENCE,
    @SerialName("sports")
    SPORTS,
    @SerialName("technology")
    TECHNOLOGY;

}
