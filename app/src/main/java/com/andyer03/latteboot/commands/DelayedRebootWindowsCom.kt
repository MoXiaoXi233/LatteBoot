package com.andyer03.latteboot.commands

import android.annotation.SuppressLint
import com.andyer03.latteboot.MainActivity
import com.andyer03.latteboot.R
import java.io.File

class DelayedRebootWindowsCom: MainActivity() {

    @SuppressLint("SdCardPath")
    fun execute() {
        // Checking for temp file existing
        val tempFile = "/sdcard/.latteboot"
        val latteboot = File(tempFile).exists()

        if (latteboot && !BootFile().check()) {
            System("win").boot()
            this.title = getString(R.string.next_boot_windows)
        } else if (latteboot && BootFile().check()) {
            System("and").boot()
            this.title = getString(R.string.next_boot_android)
        } else {
            this.title = getString(R.string.next_boot_android)
        }
    }

}