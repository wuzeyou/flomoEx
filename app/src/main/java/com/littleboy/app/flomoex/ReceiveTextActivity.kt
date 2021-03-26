package com.littleboy.app.flomoex

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.littleboy.app.flomoex.network.FlomoRepository
import com.littleboy.app.flomoex.network.FlomoService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ReceiveTextActivity: AppCompatActivity() {

    private lateinit var loading: ImageView
    private lateinit var viewModel: ReceiveTextViewModel

    companion object {
        val BASE_URL = "https://flomoapp.com"
        private const val TIME_OUT = 60
        private const val URL_PREFIX = "https://flomoapp.com/"
    }

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            //set time out
            builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

            return builder.build()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_text)
        loading = findViewById(R.id.loading_view)
        startRotateView(loading)

        val savedUrl = prefs.apiUrl
        if (!savedUrl.startsWith(URL_PREFIX)) {
            Toast.makeText(this, "未保存有效的API", Toast.LENGTH_SHORT).show()
            finish()
        }
        val validPath = savedUrl.removePrefix(URL_PREFIX)

        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build().create(FlomoService::class.java)
        val repository = FlomoRepository(service)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(ReceiveTextViewModel::class.java)

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            handleSendText(intent, validPath)
        }

        viewModel.apply {
            uiState.observe(this@ReceiveTextActivity, {
                Toast.makeText(this@ReceiveTextActivity, it.showMsg, Toast.LENGTH_SHORT).show()
                finish()
            })
        }
    }

    private fun handleSendText(intent: Intent, path: String) {
        val sendTxt = intent.getStringExtra(Intent.EXTRA_TEXT)
        sendTxt?.let {
            viewModel.postContent(path, sendTxt)
        }
    }

    override fun finish() {
        loading.clearAnimation()
        super.finish()
    }

    /**
     * 旋转动画
      */
    private fun startRotateView(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        animator.duration = 800
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }
}