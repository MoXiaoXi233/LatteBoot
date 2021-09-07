package com.andyer03.latteboot.commands

import com.andyer03.latteboot.MainActivity

class System (private val OS: String) : MainActivity() {

    fun boot() {
        when (OS) {
            "and" -> {
                BootExec().android()
            }
            "scn" -> {
                BootExec().screenoff()
            }
            "rec" -> {
                BootExec().recovery()
            }
            "fbt" -> {
                BootExec().bootloader()
            }
            "dnx" -> {
                BootExec().dnx()
            }
            "pwd" -> {
                BootExec().shutdown()
            }
            "mountefi" -> {
                BootExec().mountefi()
            }
            "sfm" -> {
                BootExec().safemode()
            }
            "win" -> {
                BootExec().windows()
            }
        }
    }
}