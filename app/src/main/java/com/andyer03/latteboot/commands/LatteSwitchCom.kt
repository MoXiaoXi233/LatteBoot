package com.andyer03.latteboot.commands

import android.annotation.SuppressLint
import java.io.File

class LatteSwitchCom {

    @SuppressLint("SdCardPath")
    fun execute() {
        // Checking for temp file existing
        val tempFile = "/sdcard/.latteboot"
        val latteboot = File(tempFile).exists()

        if (latteboot && !BootFile().check()) {
            System("win").boot()
        } else if (latteboot && BootFile().check()) {
            System("and").boot()
        } else {
            return
        }
    }
}