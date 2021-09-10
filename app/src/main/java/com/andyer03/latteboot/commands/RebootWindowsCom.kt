package com.andyer03.latteboot.commands

import android.annotation.SuppressLint
import android.widget.Toast
import com.andyer03.latteboot.MainActivity
import com.andyer03.latteboot.R
import com.andyer03.latteboot.other.Device
import java.io.File

class RebootWindowsCom: MainActivity() {

    @SuppressLint("SdCardPath")
    fun execute() {
        // Checking for temp file existing
        System("mountefi").boot()
        val tempFile = "/sdcard/.latteboot"
        val latteboot = File(tempFile).exists()
        BootExec().copytempboot()
        val file = File(Com().tempboot)
        val bootfile = Hash().crc32(file)
        if (latteboot && bootfile == Device().windowsHash) {
            System("reboot").boot()
            BootExec().deltempboot()
        } else if (latteboot && bootfile == Device().androidHash) {
            System("win").boot()
            System("reboot").boot()
            BootExec().deltempboot()
        } else {
            Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
        }
    }
}