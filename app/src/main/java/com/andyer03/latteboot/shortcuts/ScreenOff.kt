package com.andyer03.latteboot.shortcuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andyer03.latteboot.System

class ScreenOff : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        System("scn").boot()
        super.onCreate(savedInstanceState)
        finish()

    }
}