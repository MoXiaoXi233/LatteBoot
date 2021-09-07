package com.andyer03.latteboot.shortcuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andyer03.latteboot.commands.System

class RebootDevice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        System("and").boot()
        super.onCreate(savedInstanceState)
        finish()

    }
}