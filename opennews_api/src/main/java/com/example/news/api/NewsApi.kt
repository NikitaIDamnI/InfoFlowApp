@file:Suppress("unused")

package com.example.news.api

import androidx.annotation.IntRange
import com.example.news.api.models.ArticleDTO
import com.example.news.api.models.Language
import com.example.news.api.models.ResponseDTO
import com.example.news.api.models.SortByDto
import com.example.news.api.utils.NewsApiKeyInterceptor
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.format.DateTimeFormatter

/**
 * [API Documentation](https://newsapi.org/docs/get-started)
 */
interface NewsApi {
    /**
     * API details [here](https://newsapi.org/docs/endpoints/everything)
     */
    @GET("everything")
    @Suppress("LongParameterList")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("languages") languages: List<@JvmSuppressWildcards Language>? = null,
        @Query("sortBy") sortBy: SortByDto? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1
    ): Result<ResponseDTO<ArticleDTO>>

    @GET("top-headlines")
    @Suppress("LongParameterList")
    suspend fun topHeadlines(
        @Query("q") query: String? = null,
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("sources") sources: String? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1
    ): Result<ResponseDTO<ArticleDTO>>

}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): NewsApi {
    return retrofit(baseUrl, apiKey, okHttpClient, json).create()
}

private fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient?,
    json: Json
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    val modifiedOkHttpClient =
        (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
            .addInterceptor(NewsApiKeyInterceptor(apiKey))
            .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedOkHttpClient)
        .build()
}

fun formatterApi() = DateTimeFormatter.ofPattern("yyyy-MM-dd")
