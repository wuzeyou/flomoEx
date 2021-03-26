package com.littleboy.app.flomoex.network

import okhttp3.RequestBody
import retrofit2.http.*

interface FlomoService {
    @POST("/{path}")
    suspend fun postText(
        @Path(value = "path", encoded = true) path: String,
        @Body body : RequestBody
    ): FlomoResponse
}