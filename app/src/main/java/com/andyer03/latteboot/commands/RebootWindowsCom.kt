package com.andyer03.latteboot.commands

import android.annotation.SuppressLint
import java.io.File

class RebootWindowsCom {

    @SuppressLint("SdCardPath")
    fun execute() {
        // Checking for temp file existing
        val tempFile = "/sdcard/.latteboot"
        val latteboot = File(tempFile).exists()
        when {
            latteboot && BootFile().check() == "Windows" -> {
                System("reboot").boot()
            }
            latteboot && BootFile().check() == "Android" -> {
                System("win").boot()
                System("reboot").boot()
            }
            else -> {
                return
            }
        }
    }
}