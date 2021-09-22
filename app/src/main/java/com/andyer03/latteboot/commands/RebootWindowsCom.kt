package com.andyer03.latteboot.commands

class RebootWindowsCom {
    fun execute() {
        when {
            BootFile().check() == "Windows" -> {
                System("reboot").boot()
            }
            BootFile().check() == "Android" -> {
                System("win").boot()
                System("reboot").boot()
            }
            else -> {
                return
            }
        }
    }
}