package com.littleboy.app.flomoex

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class FromLauncherActivity: AppCompatActivity() {

    companion object {
        val LAUNCHER_ACTION = "flomoex.action.LAUNCHER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_from_launcher)
        val confrimBtn: View = findViewById(R.id.confirm_button)
        confrimBtn.setOnClickListener {
            finish()
        }
    }
}