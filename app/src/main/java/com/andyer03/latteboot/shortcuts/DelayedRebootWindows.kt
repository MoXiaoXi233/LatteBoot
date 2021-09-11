package com.andyer03.latteboot.shortcuts

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.andyer03.latteboot.R
import com.andyer03.latteboot.commands.*

class DelayedRebootWindows : AppCompatActivity() {
    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {

        DelayedRebootWindowsCom().execute()
        if (!BootFile().check()) {
            Toast.makeText(this, R.string.next_boot_android, Toast.LENGTH_SHORT).show()
        } else if (BootFile().check()) {
            Toast.makeText(this, R.string.next_boot_windows, Toast.LENGTH_SHORT).show()
        } else {
            return
        }
        finish()
        super.onCreate(savedInstanceState)
    }
}