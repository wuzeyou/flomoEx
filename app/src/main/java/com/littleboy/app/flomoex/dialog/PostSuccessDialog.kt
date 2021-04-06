package com.littleboy.app.flomoex.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.littleboy.app.flomoex.R
import java.lang.Exception

class PostSuccessDialog: DialogFragment() {

    companion object {
        fun newInstance(msg: String): PostSuccessDialog {
            val fragment = PostSuccessDialog()
            val bundle = Bundle()
            bundle.putCharSequence("message", msg)
            fragment.arguments = bundle
            fragment.setStyle(STYLE_NO_FRAME, R.style.base_dialog)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_post_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val msg = it.getCharSequence("message")
            view.findViewById<TextView>(R.id.content_msg).text = msg
        }
        view.findViewById<View>(R.id.confirm_button).setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = (if (activity == null) null else activity!!.window) ?: return
        //解决全屏activity下状态栏会变色的bug
        //仅在全屏的activity中应用以下代码，防止在HomeActivity中使用状态栏会异常
        if (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN
            == View.SYSTEM_UI_FLAG_FULLSCREEN
        ) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                    val field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                    field.isAccessible = true
                    field.setInt(window.decorView, Color.TRANSPARENT) //去掉高版本蒙层改为透明
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onResume() {
        if (dialog != null && dialog!!.window != null) {
            val p = dialog!!.window!!.attributes
            val dm = this.context!!.resources.displayMetrics
            p.height = WindowManager.LayoutParams.WRAP_CONTENT
            p.width = (dm.widthPixels * 0.8).toInt()
            dialog!!.window!!.attributes = p
        }
        super.onResume()
    }
}