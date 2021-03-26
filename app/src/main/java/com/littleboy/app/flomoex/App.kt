package com.littleboy.app.flomoex

import android.app.Application
import com.littleboy.app.flomoex.base.Prefs

val prefs: Prefs by lazy {
    App.prefs!!
}

class App: Application() {

    companion object {
        var prefs: Prefs? = null
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = Prefs(applicationContext)
    }
}