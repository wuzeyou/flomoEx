package com.littleboy.app.flomoex

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context

class ClipboardHelper private constructor(context: Context?) {

    val TAG = "ClipboardHelper"

    private var mContext = context

    private var mClipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    companion object {
        /**
         * 获取ClipboardUtil实例
         */
        fun getInstance(context: Context): ClipboardHelper {
            val instance: ClipboardHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ClipboardHelper(context) }
            return instance
        }
    }



    /**
     * 判断剪贴板内是否有数据
     *
     * @return
     */
    fun hasPrimaryClip(): Boolean {
        return mClipboardManager.hasPrimaryClip()
    }

    /**
     * 获取剪贴板中第一条String
     *
     * @return
     */
    fun getClipText(): String? {
        if (!hasPrimaryClip()) {
            return null
        }
        val data = mClipboardManager.primaryClip ?: return null
        return if (data.getItemAt(0) != null) {
            if (data.getItemAt(0).text != null) {
                data.getItemAt(0).text.toString()
            } else {
                null
            }
        } else null
    }


    /**
     * 获取剪贴板中的HTMLText
     *
     * @return
     */
    fun getClipHTMLText(): String? {
        if (!hasPrimaryClip()) {
            return null
        }
        val data = mClipboardManager.primaryClip
        return if (data != null && mClipboardManager.primaryClipDescription != null
            && mClipboardManager.primaryClipDescription!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)
        ) {
            data.getItemAt(0).htmlText
        } else {
            null
        }
    }

    /**
     * 将文本拷贝至剪贴板
     *
     * @param text
     */
    fun copyText(text: String?) {
        copyText("yoha", text)
    }


    /**
     * 将文本拷贝至剪贴板
     * @param label 自定义标签
     * @param text
     */
    fun copyText(label: String?, text: String?) {
        val clip = ClipData.newPlainText(label, text)
        mClipboardManager.setPrimaryClip(clip)
    }

}