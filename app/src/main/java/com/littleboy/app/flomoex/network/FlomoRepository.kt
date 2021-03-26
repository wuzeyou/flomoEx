package com.littleboy.app.flomoex.network

import com.google.gson.JsonObject
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType
import okhttp3.RequestBody

class FlomoRepository constructor(
    private val service: FlomoService
) {

    suspend fun postToFlomo(path: String, content: String) : FlomoResponse {
        val jsonObject = JsonObject()
        jsonObject.addProperty("content", content)
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(mediaType, jsonObject.toString())
        return coroutineScope {
            service.postText(path, body)
        }
    }
}