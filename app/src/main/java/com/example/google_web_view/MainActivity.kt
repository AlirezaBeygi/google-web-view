package com.example.google_web_view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
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

    class GeoWebChromeClient : WebChromeClient() {
        @SuppressLint("SetJavaScriptEnabled")
        override fun onCreateWindow(
            view: WebView?, isDialog: Boolean,
            isUserGesture: Boolean, resultMsg: Message
        ): Boolean {
            //            view.addView(bitWebView);

            val transport = resultMsg.obj as WebViewTransport
            transport.webView = view
            resultMsg.sendToTarget()

            /*bitWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });*/
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mySwipeRefreshLayout = binding.swipeContainer

        val url = "https://github.com/"

        webView = binding.webView

        webView.getSettings().setLoadsImagesAutomatically(true)

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET)

        webView.getSettings().setJavaScriptEnabled(true)

        webView.getSettings().setSupportMultipleWindows(false)

        webView.getSettings().setDomStorageEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                // نمایش پیام خطا
                Toast.makeText(this@MainActivity, "Error: $description", Toast.LENGTH_SHORT).show()
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            webView.getSettings().setForceDark(WebSettings.FORCE_DARK_OFF)
        }

        mySwipeRefreshLayout!!.setOnRefreshListener {
            mySwipeRefreshLayout!!.isRefreshing = true
            webView.reload()
            mySwipeRefreshLayout!!.isRefreshing = false
        }

        webView.clearCache(true)

        // Configure WebView settings
        webView.settings.apply {
            loadsImagesAutomatically = true
            javaScriptCanOpenWindowsAutomatically = true
            javaScriptEnabled = true
            setSupportMultipleWindows(false)
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        }

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.clearHistory()
        webView.clearCache(true)
        webView.clearFormData()

        webView.webChromeClient = GeoWebChromeClient()

        webView.loadUrl("file:///android_asset/index.html")

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