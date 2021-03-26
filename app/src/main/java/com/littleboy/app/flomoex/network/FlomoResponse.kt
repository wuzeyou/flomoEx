package com.littleboy.app.flomoex.network

import com.google.gson.annotations.SerializedName

data class FlomoResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
