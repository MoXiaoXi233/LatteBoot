package com.andyer03.latteboot.commands

class LatteSwitchCom {
    fun execute() {
        // Checking for temp file existing
        if (BootFile().check()) {
            System("and").boot()
        } else if (!BootFile().check()) {
            System("win").boot()
        }
    }
}