package com.andyer03.latteboot

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.io.File

class RebootWindows : AppCompatActivity() {
    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {

        // Checking for temp file existing
        val tempFile = "/sdcard/.latteboot"
        val latteboot = File(tempFile).exists()
        if (latteboot) {
            System("mountefi").boot()
            System("win").boot()
        } else {
            Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()

            // Hide app from drawer
            val p = packageManager
            val componentName = ComponentName(applicationContext, RebootWindows::class.java)
            p.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
        super.onCreate(savedInstanceState)
        finish()

    }
}