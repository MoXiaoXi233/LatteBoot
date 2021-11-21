package com.andyer03.latteboot.shortcuts

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andyer03.latteboot.R
import com.andyer03.latteboot.commands.BootAnotherMode
import com.andyer03.latteboot.commands.BootFile

@ExperimentalStdlibApi
class RebootWindows : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        BootAnotherMode().boot("Windows", "Android", "reboot", "win")
        if (BootFile().check() != "Windows") {
            Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
        }
        finish()
        super.onCreate(savedInstanceState)
    }
}