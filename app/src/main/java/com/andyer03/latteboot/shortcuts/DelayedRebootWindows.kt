package com.andyer03.latteboot.shortcuts

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andyer03.latteboot.commands.*

class DelayedRebootWindows : AppCompatActivity() {
    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {

        DelayedRebootWindowsCom().execute()

        super.onCreate(savedInstanceState)
    }
}