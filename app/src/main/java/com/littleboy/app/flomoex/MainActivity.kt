package com.littleboy.app.flomoex

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import android.content.Intent
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.rengwuxian.materialedittext.MaterialEditText
import com.rengwuxian.materialedittext.validation.RegexpValidator
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val CATEGORY_TEXT_SHARE_TARGET = "com.littleboy.app.flomoex.sharingshortcuts.TEXT_SHARE_TARGET"

    private lateinit var inputEdit: MaterialEditText
    private lateinit var confirmBtn: Button
    val prefixValidator = RegexpValidator("格式错误，请检查后重试", """^https:\/\/flomoapp.com\/(.)*""")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        pushShareShortcut()
    }

    private fun initView() {
        inputEdit = findViewById(R.id.input)
        confirmBtn = findViewById(R.id.confirm_button)
        inputEdit.addValidator(prefixValidator)

        inputEdit.setText(prefs.apiUrl)

        confirmBtn.setOnClickListener {
            if (inputEdit.validate()) {
                prefs.apiUrl = inputEdit.text.toString()
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "校验失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun pushShareShortcut() {
        val staticLauncherShortcutIntent = Intent(FromLauncherActivity.LAUNCHER_ACTION)
        val contactCategories: MutableSet<String> = HashSet()
        contactCategories.add(CATEGORY_TEXT_SHARE_TARGET)

        val shortCutInfo = ShortcutInfoCompat.Builder(this, "102")
            .setShortLabel("发送到flomo")
            .setLongLabel("发送到flomo")
            .setIcon(IconCompat.createWithResource(this, R.mipmap.flomo_launcher))
            .setIntent(staticLauncherShortcutIntent)
            .setLongLived(true)
            .setCategories(contactCategories)
            .build()

        val shortcuts: ArrayList<ShortcutInfoCompat> = ArrayList()
        shortcuts.add(shortCutInfo)

        ShortcutManagerCompat.addDynamicShortcuts(this, shortcuts)
    }

    private fun drawableToBitmap(drawable: Drawable, context: Context): Bitmap? {
        val screenDensity: Float = context.resources.displayMetrics.density
        val adaptiveIconSize = (72 * screenDensity).roundToInt()
        val adaptiveIconOuterSides = (108 * screenDensity).roundToInt()
        val bitmap = Bitmap.createBitmap(adaptiveIconSize, adaptiveIconSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(
            adaptiveIconOuterSides, adaptiveIconOuterSides, adaptiveIconSize - adaptiveIconOuterSides,
            adaptiveIconSize - adaptiveIconOuterSides
        )
        drawable.draw(canvas)
        return bitmap
    }
}