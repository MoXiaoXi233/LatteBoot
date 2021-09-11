package com.andyer03.latteboot.shortcuts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.andyer03.latteboot.R
import com.andyer03.latteboot.commands.Root
import com.andyer03.latteboot.commands.System

class RebootSafeMode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if (Root().check()) {
            System("sfm").boot()
        } else {
            Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
        }
        super.onCreate(savedInstanceState)
        finish()

    }
}