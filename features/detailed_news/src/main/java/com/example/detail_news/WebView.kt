package com.example.detail_news

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.State
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewCompose(htmlContent: String) {
    Log.d("Recomposition", "WebViewCompose")

        AndroidView(factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true // Включаем JavaScript
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        return true
                    }
                }
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        })

}