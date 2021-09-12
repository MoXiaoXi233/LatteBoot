package com.andyer03.latteboot.shortcuts

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.andyer03.latteboot.R
import com.andyer03.latteboot.commands.*

class RebootWindows : AppCompatActivity() {
    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {

        if (Root().check()) {
            RebootWindowsCom().execute()
            if (!BootFile().check()) {
                Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
        }
        finish()
        super.onCreate(savedInstanceState)
    }
}