package com.example.google_web_view

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.google_web_view.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView

    var mySwipeRefreshLayout: SwipeRefreshLayout? = null

    var doubleClick: Boolean = false

    private lateinit var binding: ActivityMainBinding

    class GeoWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // When user clicks a hyperlink, load in the existing WebView
            view.loadUrl(url)
            return true
        }
    }

    override fun onStart() {
        super.onStart()
        doubleClick = false
    }


    override fun onResume() {
        super.onResume()
        doubleClick = false
    }

    override fun onRestart() {
        super.onRestart()
        doubleClick = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mySwipeRefreshLayout = binding.swipeContainer

        val url = "http://nbit.omidpayment.ir/"

        webView = binding.webView

        webView.getSettings().setLoadsImagesAutomatically(true)

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET)

        webView.getSettings().setJavaScriptEnabled(true)

        webView.getSettings().setSupportMultipleWindows(false)

        webView.getSettings().setDomStorageEnabled(true)

        webView.setWebViewClient(GeoWebViewClient())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            webView.getSettings().setForceDark(WebSettings.FORCE_DARK_OFF)
        }

        mySwipeRefreshLayout!!.setOnRefreshListener {
            mySwipeRefreshLayout!!.isRefreshing = true
            webView.reload()
            mySwipeRefreshLayout!!.isRefreshing = false
        }

        webView.loadUrl(url)

        try {
            Thread.sleep(500)
            doubleClick = false
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }
    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else if (doubleClick) super.onBackPressed()
        else {
            Toast.makeText(this, "برای خروج یکبار دیگر کلیک کنید", Toast.LENGTH_SHORT).show()
            doubleClick = true
        }
    }
}