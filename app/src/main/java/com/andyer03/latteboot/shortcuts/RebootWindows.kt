package com.andyer03.latteboot.shortcuts

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.andyer03.latteboot.R
import com.andyer03.latteboot.commands.System
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
        }
        super.onCreate(savedInstanceState)
        finish()

    }
}