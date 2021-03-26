package com.littleboy.app.flomoex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.littleboy.app.flomoex.base.BaseViewModel
import com.littleboy.app.flomoex.network.FlomoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class ReceiveTextViewModel(private val repository: FlomoRepository) : BaseViewModel() {
    //界面的提示信息，发送后的提示
    private val _uiState = MutableLiveData<FlomoUiMessage>()
    val uiState: LiveData<FlomoUiMessage>
        get() = _uiState

    fun postContent(path: String, content: String) {
        launchOnUI {
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.postToFlomo(path, content)
                }
                emitUiState(result.code == 0, result.message)
            } catch (e: Exception) {
                if (e is HttpException) {
                    val jsonStr: String = e.response()?.errorBody()?.string() ?: "{}"
                    try {
                        val resObj = JSONObject(jsonStr)
                        emitUiState(false, resObj.optString("message"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emitUiState(false, "解析失败")
                    }
                } else {
                    e.printStackTrace()
                    emitUiState(false, "发送失败")
                }
            }
        }
    }

    private fun emitUiState(
        success: Boolean,
        msg: String? = null
    ) {
        val uiMessage = FlomoUiMessage(success, msg)
        _uiState.value = uiMessage
    }
}

data class FlomoUiMessage(
    val success: Boolean,
    val showMsg: String?
)