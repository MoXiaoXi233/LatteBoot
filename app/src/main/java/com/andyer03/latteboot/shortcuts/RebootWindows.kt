package com.andyer03.latteboot.shortcuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.andyer03.latteboot.R
import com.andyer03.latteboot.commands.*

class RebootWindows : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Root().check()) {
            BootAnotherOS().windows()
            if (BootFile().check() != "Windows") {
                Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
        }
        finish()
        super.onCreate(savedInstanceState)
    }
}