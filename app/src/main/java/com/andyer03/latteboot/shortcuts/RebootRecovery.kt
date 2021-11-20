package com.andyer03.latteboot.shortcuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andyer03.latteboot.commands.System

@ExperimentalStdlibApi
class RebootRecovery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        System("rec").boot()
        super.onCreate(savedInstanceState)
        finish()

    }
}