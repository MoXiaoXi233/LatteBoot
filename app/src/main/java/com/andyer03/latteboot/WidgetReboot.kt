package com.andyer03.latteboot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WidgetReboot : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_reboot)

        System("mountefi").boot()
        System("win").boot()
    }
}