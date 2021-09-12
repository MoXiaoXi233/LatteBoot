package com.andyer03.latteboot.commands

import android.annotation.SuppressLint

class RebootAndroidCom {

    @SuppressLint("SdCardPath")
    fun execute() {
        if (!BootFile().check()) {
            System("reboot").boot()
        } else if (BootFile().check()) {
            System("and").boot()
            System("reboot").boot()
        } else {
            return
        }
    }
}