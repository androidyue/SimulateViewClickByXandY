package oid.dr.an.simulatetoucheventsample

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var webview: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        webview = findViewById(R.id.webview)
        webview?.settings?.javaScriptEnabled = true
        webview?.loadUrl("http://v.qq.com/iframe/player.html?vid=o0600gxaoxo&auto=1")

        webview?.setOnTouchListener { v, event ->
            debugMessage("onTouchListener x=${event.x};y=${event.y}")
            false
        }

        webview?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                webview?.post {
                    webview?.let {
                        simulateTouchEvent(it, it.width / 2f, it.height / 2f)
                    }
                }
            }
        }

        fab.setOnClickListener { view ->
            webview?.let {
                simulateTouchEvent(it, it.width / 2f, it.height / 2f)
            }
        }
    }

    private fun simulateTouchEvent(view: View, x: Float, y: Float) {
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 100
        val metaState = 0
        val motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, metaState)

        view.dispatchTouchEvent(motionEvent)

        val upEvent = MotionEvent.obtain(downTime + 1000, eventTime + 1000,
                MotionEvent.ACTION_UP, x,y, metaState)
        view.dispatchTouchEvent(upEvent)
    }


    private fun debugMessage(msg: String) {
        Log.d("MainActivity", msg)
    }

}
