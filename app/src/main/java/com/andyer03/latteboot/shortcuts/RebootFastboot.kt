package com.andyer03.latteboot.shortcuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andyer03.latteboot.commands.System

class RebootFastboot : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        System("fbt").boot()
        super.onCreate(savedInstanceState)
        finish()

    }
}