package com.andyer03.latteboot.commands

@ExperimentalStdlibApi
class BootAnotherOS {

    fun android() {
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

    fun windows() {
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