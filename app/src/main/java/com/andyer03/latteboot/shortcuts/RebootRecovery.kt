package com.andyer03.latteboot.shortcuts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andyer03.latteboot.commands.BootAnotherMode

@ExperimentalStdlibApi
class RebootRecovery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        BootAnotherMode().boot("Windows", "Android", "rec", "and")
        super.onCreate(savedInstanceState)
        finish()
    }
}