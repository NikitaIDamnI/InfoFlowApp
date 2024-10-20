package com.example.detailNews

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewCompose(
    htmlContent: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        return true
                    }
                }
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        })
    }
}
