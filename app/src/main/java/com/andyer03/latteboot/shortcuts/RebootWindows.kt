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
        if (BootFile().checkWin()) {
            BootAnotherMode().boot("Windows", "Android", "reboot", "win")
            if (BootFile().checkBoot() != "Windows") {
                toast()
            }
        } else {
            toast()
        }
        finish()
        super.onCreate(savedInstanceState)
    }

    private fun toast() {
        Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
    }
}