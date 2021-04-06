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
import com.littleboy.app.flomoex.dialog.PostSuccessDialog
import com.littleboy.app.flomoex.network.FlomoRepository
import com.littleboy.app.flomoex.network.FlomoService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ReceiveTextActivity: AppCompatActivity() {

    private lateinit var loading: ImageView
    private lateinit var viewModel: ReceiveTextViewModel

    private var sendText: String? = null

    companion object {
        val BASE_URL = "https://flomoapp.com"
        private const val TIME_OUT = 60
        const val URL_PREFIX = "https://flomoapp.com/"
        const val QUICK_CLIPBOARD_ACTION = "flomoex.action.quick.clipboard"
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

        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build().create(FlomoService::class.java)
        val repository = FlomoRepository(service)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(ReceiveTextViewModel::class.java)

        window.decorView.post {
            handleSendText(intent)
        }

        viewModel.apply {
            uiState.observe(this@ReceiveTextActivity, {
                val dialog = sendText?.let { it1 -> PostSuccessDialog.newInstance(it1) }
                dialog?.show(supportFragmentManager, null)
            })
        }
    }

    private fun handleSendText(intent: Intent?) {
        val savedUrl = prefs.apiUrl
        if (!savedUrl.startsWith(URL_PREFIX)) {
            Toast.makeText(this, "未保存有效的API", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val validPath = savedUrl.removePrefix(URL_PREFIX)
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            // from share sheet
            sendText = intent.getStringExtra(Intent.EXTRA_TEXT)
        } else if (intent?.action == QUICK_CLIPBOARD_ACTION) {
            val clipboardHelper = ClipboardHelper.getInstance(context = this)
            sendText = clipboardHelper.getClipText()
        }
        if (sendText != null) {
            viewModel.postContent(validPath, sendText!!)
        } else {
            Toast.makeText(this, "请先复制一段文字后再发送", Toast.LENGTH_SHORT).show()
            finish()
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