package com.andyer03.latteboot.commands

class LatteSwitchCom {
    fun execute() {
        // Checking for temp file existing
        if (BootFile().check() == "Windows") {
            System("and").boot()
        } else if (BootFile().check() == "Android") {
            System("win").boot()
        }
    }
}