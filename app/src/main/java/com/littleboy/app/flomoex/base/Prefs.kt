package com.littleboy.app.flomoex.base

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    private val API_URL = "api_url"

    private val preferences: SharedPreferences = context.getSharedPreferences("flomoex", Context.MODE_PRIVATE)

    var apiUrl: String
        get() = preferences.getString(API_URL, "") ?: ""
        set(value) = preferences.edit().putString(API_URL, value).apply()
}