package com.andyer03.latteboot.commands

import android.annotation.SuppressLint

class RebootAndroidCom {

    @SuppressLint("SdCardPath")
    fun execute() {
        when {
            BootFile().check() == "Android" -> {
                System("reboot").boot()
            }
            BootFile().check() == "Windows" -> {
                System("and").boot()
                System("reboot").boot()
            }
            else -> {
                return
            }
        }
    }
}