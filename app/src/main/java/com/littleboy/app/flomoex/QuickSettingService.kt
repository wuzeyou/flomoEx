package com.littleboy.app.flomoex

import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class QuickSettingService: TileService() {

    override fun onClick() {
        val intent = Intent(ReceiveTextActivity.QUICK_CLIPBOARD_ACTION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityAndCollapse(intent)
    }
}