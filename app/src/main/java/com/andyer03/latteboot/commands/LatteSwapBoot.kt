package com.andyer03.latteboot.commands

@ExperimentalStdlibApi
class LatteSwapBoot {
    fun swap() {
        // Checking for temp file existing
        if (BootFile().check() == "Windows") {
            System("and").boot()
        } else if (BootFile().check() == "Android") {
            System("win").boot()
        }
    }
}